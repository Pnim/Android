package com.example.voiceassistent;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences localPreferences;
    private MessageAdapter messageAdapter;
    private Button sendQuestionButton;
    private RecyclerView messageList;
    private SQLiteDatabase provider;
    private EditText questionField;
    private TextToSpeech speaker;
    private Boolean isDayTheme;
    private DBContext context;

    public final String APP_PREFERENCES = "settingsName";
    private final String THEME = "THEME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        localPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        isDayTheme = localPreferences.getBoolean(THEME, true);
        getDelegate().setLocalNightMode(getThemeMode(isDayTheme));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        context = new DBContext(this);
        messageAdapter = new MessageAdapter();
        sendQuestionButton = findViewById(R.id.sendQuestionButton);
        questionField = findViewById(R.id.questionField);
        messageList = findViewById(R.id.messageList);
        messageList.setLayoutManager(layoutManager);
        messageList.setAdapter(messageAdapter);
        provider = context.getWritableDatabase();

        Cursor cursor = provider.query(context.TABLE_NAME, null, null, null, null, null, null);
        fillMessages(cursor);
        cursor.close();

        sendQuestionButton.setOnClickListener(view -> processSendMessageBySuperPuperAI());
        speaker = new TextToSpeech(getApplicationContext(), i -> {
            if(i != TextToSpeech.ERROR){
                speaker.setLanguage(new Locale("ru"));
            }
        });
    }

    private int getThemeMode(Boolean isDayTheme) {
        if(isDayTheme) {
            return AppCompatDelegate.MODE_NIGHT_NO;
        }
        else {
            return AppCompatDelegate.MODE_NIGHT_YES;
        }
    }

    private void processSendMessageBySuperPuperAI() {
        final String question = questionField.getText().toString();
        try {
            final String resultQuestion = SuperPuperRoboAI.replaceWhiteSpace(question);
            messageAdapter.messages.add(new Message(resultQuestion, Calendar.getInstance().getTime(),true));
            messageAdapter.notifyDataSetChanged();
            messageList.scrollToPosition(messageAdapter.messages.size() - 1);
            questionField.setText("");
            SuperPuperRoboAI.processQuestion(resultQuestion, str -> {
                messageAdapter.messages.add(new Message(str, Calendar.getInstance().getTime(), false));
                speaker.speak(str, TextToSpeech.QUEUE_FLUSH, null, null);
                messageAdapter.notifyDataSetChanged();
                messageList.scrollToPosition(messageAdapter.messages.size() - 1);
                questionField.setText("");
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void fillMessages(Cursor cursor) {
        if (cursor.moveToFirst()) {
            int msgIndex = cursor.getColumnIndex(context.FIELD_MESSAGE);
            int dateTimeIndex = cursor.getColumnIndex(context.FIELD_DATETIME);
            int wasSendIndex = cursor.getColumnIndex(context.FIELD_SEND);
            do {
                try {
                    MessageModel model = new MessageModel(cursor.getString(msgIndex), cursor.getString(dateTimeIndex), cursor.getInt(wasSendIndex));
                    messageAdapter.messages.add(new Message(model));
                } catch(ParseException e) {
                    e.printStackTrace();
                }
            } while(cursor.moveToNext());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = localPreferences.edit();
        editor.putBoolean(THEME, isDayTheme);
        editor.apply();
        provider.delete(context.TABLE_NAME, null, null);
        for (int i = 0; i < messageAdapter.messages.size(); i++){
            MessageModel entity = new MessageModel(messageAdapter.messages.get(i));
            ContentValues contentValues = new ContentValues();
            contentValues.put(context.FIELD_MESSAGE, entity.text);
            contentValues.put(context.FIELD_SEND, entity.wasSend);
            contentValues.put(context.FIELD_DATETIME, entity.dateTime);

            provider.insert(context.TABLE_NAME, null, contentValues);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putSerializable("Iitschaat", messageAdapter);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        messageAdapter = (MessageAdapter) (state.getSerializable("Iitschaat"));
        messageList.setAdapter(messageAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu current){
        getMenuInflater().inflate(R.menu.menu, current);
        return super.onCreateOptionsMenu(current);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.dayTheme:
                isDayTheme = true;
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case R.id.nightTheme:
                isDayTheme = false;
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
