package ca.mcgill.ecse321.eventregistration_android;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v4.app.FragmentActivity;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    private String error = null;
    // APPEND NEW CONTENT STARTING FROM HERE
    private List<String> participantNames = new ArrayList<>();
    private ArrayAdapter<String> participantAdapter;
    private List<String> eventNames = new ArrayList<>();
    private ArrayAdapter<String> eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        refreshErrorMessage();

        Spinner participantSpinner = (Spinner) findViewById(R.id.participantspinner);
        Spinner eventSpinner = (Spinner) findViewById(R.id.eventspinner);

        participantAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, participantNames);
        participantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        participantSpinner.setAdapter(participantAdapter);

        eventAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, eventNames);
        eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSpinner.setAdapter(eventAdapter);

        // Get initial content for spinners
        refreshLists(this.getCurrentFocus());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshErrorMessage() {
        // set the error message
        TextView tvError = (TextView) findViewById(R.id.error);
        tvError.setText(error);

        if (error == null || error.length() == 0) {
            tvError.setVisibility(View.GONE);
        } else {
            tvError.setVisibility(View.VISIBLE);
        }

    }

    public void addParticipant(View v) {
        error = "";
        final TextView tv = (TextView) findViewById(R.id.newparticipant_name);
        HttpUtils.post("participants/" + tv.getText().toString(), new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                refreshErrorMessage();
                tv.setText("");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("message").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }
                refreshErrorMessage();
            }
        });
    }

    private Bundle getTimeFromLabel(String text) {
        Bundle rtn = new Bundle();
        String comps[] = text.toString().split(":");
        int hour = 12;
        int minute = 0;

        if (comps.length == 2) {
            hour = Integer.parseInt(comps[0]);
            minute = Integer.parseInt(comps[1]);
        }

        rtn.putInt("hour", hour);
        rtn.putInt("minute", minute);

        return rtn;
    }

    private Bundle getDateFromLabel(String text) {
        Bundle rtn = new Bundle();
        String comps[] = text.toString().split("-");
        int day = 1;
        int month = 1;
        int year = 1;

        if (comps.length == 3) {
            day = Integer.parseInt(comps[0]);
            month = Integer.parseInt(comps[1]);
            year = Integer.parseInt(comps[2]);
        }

        rtn.putInt("day", day);
        rtn.putInt("month", month-1);
        rtn.putInt("year", year);

        return rtn;
    }

    public void showTimePickerDialog(View v) {
        TextView tf = (TextView) v;
        Bundle args = getTimeFromLabel(tf.getText().toString());
        args.putInt("id", v.getId());

        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setArguments(args);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        TextView tf = (TextView) v;
        Bundle args = getDateFromLabel(tf.getText().toString());
        args.putInt("id", v.getId());

        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(args);
        newFragment.show(getFragmentManager(), "datePicker");       //change from getSupportFragmentManager()
    }

    public void setTime(int id, int h, int m) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(String.format("%02d:%02d", h, m));
    }

    public void setDate(int id, int d, int m, int y) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(String.format("%02d-%02d-%04d", d, m + 1, y));
    }

    public void register(View v) {

        Spinner partSpinner = (Spinner) findViewById(R.id.participantspinner);
        Spinner eventSpinner = (Spinner) findViewById(R.id.eventspinner);

        error = "";
        String pName = partSpinner.getSelectedItem().toString();
        String eName = eventSpinner.getSelectedItem().toString();

        RequestParams rp = new RequestParams();
        rp.add("participant", pName);
        rp.add("event", eName);

        // TODO issue an HTTP POST here
        HttpUtils.post("register", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                refreshErrorMessage();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("message").toString();//virtual method on a mull obj reference?
                } catch (JSONException e) {
                    error += e.getMessage();
                }
                refreshErrorMessage();
            }
        });

        // Set back the spinners to the initial state after posting the request
        partSpinner.setSelection(0);
        eventSpinner.setSelection(0);

        refreshErrorMessage();
    }

    public void refreshLists(View v){
        refreshList(participantAdapter ,participantNames, "participants");
        refreshList(eventAdapter, eventNames, "events");
    }

    private void refreshList(final ArrayAdapter<String> adapter, final List<String> names, String restFunctionName) {
        HttpUtils.get(restFunctionName, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                names.clear();
                names.add("Please select...");
                for( int i = 0; i < response.length(); i++){
                    try {
                        names.add(response.getJSONObject(i).getString("name"));
                    } catch (Exception e) {
                        error += e.getMessage();
                    }
                    refreshErrorMessage();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("message").toString();//virtual method on a mull obj reference?
                } catch (JSONException e) {
                    error += e.getMessage();
                }
                refreshErrorMessage();
            }
        });
    }

    public void addEvent(View v) {
        // start time
        TextView tv = (TextView) findViewById(R.id.starttime);
        String text = tv.getText().toString();
        String comps[] = text.split(":");

        int startHours = Integer.parseInt(comps[0]);
        int startMinutes = Integer.parseInt(comps[1]);

        // TODO get end time
        TextView tv1 = (TextView) findViewById(R.id.endtime);
        String text1 = tv1.getText().toString();
        String comps1[] = text1.split(":");

        int endHours = Integer.parseInt(comps1[0]);
        int endMinutes = Integer.parseInt(comps1[1]);

        // date
        tv = (TextView) findViewById(R.id.newevent_date);
        text = tv.getText().toString();
        comps = text.split("-");

        int year = Integer.parseInt(comps[2]);
        int month = Integer.parseInt(comps[1]);
        int day = Integer.parseInt(comps[0]);

        // name
        tv = (TextView) findViewById(R.id.newevent_name);
        String name = tv.getText().toString();

        // Reminder: calling the service looks like this:
        // http://192.168.56.50:8088/createEvent?eventName=tst&date=2013-10-23&startTime=00:00&endTime=23:59

        RequestParams rp = new RequestParams();

        NumberFormat formatter = new DecimalFormat("00");
        rp.add("date", year + "-" + formatter.format(month) + "-" + formatter.format(day));
        rp.add("startTime", formatter.format(startHours) + ":" + formatter.format(startMinutes));
        // TODO add end time as parameter
        rp.add("endTime", formatter.format(endHours) + ":" + formatter.format(endMinutes));
        HttpUtils.post("events/" + name, rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                refreshErrorMessage();
                ((TextView) findViewById(R.id.newevent_name)).setText("");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("message").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }
                refreshErrorMessage();
            }
        });
    }

}