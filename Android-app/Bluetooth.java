package com.aput.one;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.ToggleButton;

import android.widget.Toast;
import java.util.ArrayList;
import java.util.Set;

public class Bluetooth extends AppCompatActivity {
    private BluetoothAdapter BA;
    private Set<BluetoothDevice>pairedDevices;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        BA = BluetoothAdapter.getDefaultAdapter();
        lv = (ListView)findViewById(R.id.listView);

        ToggleButton b_bt = (ToggleButton) findViewById(R.id.toggleButton);
        b_bt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!BA.isEnabled()) {
                        Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(turnOn, 1);
                    } else {
                        Toast.makeText(getApplicationContext(), "Already On" ,Toast.LENGTH_SHORT).show();
                    }

                } else {
                    BA.disable();
                    Toast.makeText(getApplicationContext(), "Turned off" ,Toast.LENGTH_SHORT).show();

                }

                list();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {                                     //checks that Bluetooth is actually enabled, otherwise paired device list doesn't work
        super.onActivityResult(requestCode, resultCode, data);                                                          //necessary because Bluetooth process is done in another thread (need to synchronize, can't print list earlier than it finishes)

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Turned On" ,Toast.LENGTH_SHORT).show();
                list();
            } else {
                Toast.makeText(getApplicationContext(), "Cancelled" ,Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void list(){                                                                                                 //listView method
        pairedDevices = BA.getBondedDevices();
        ArrayList list = new ArrayList();
        for(BluetoothDevice bt : pairedDevices) list.add(bt.getName() + "\n" + bt.getAddress());
        final ArrayAdapter adapter = new  ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long l) {
            Object item = adapter.getItem(position);
            String Device_Name = item.toString();
            Device_Name=Device_Name.substring(0, Device_Name.length()-18);

            Intent intent = new Intent(Bluetooth.this, Timer.class);
            intent.putExtra("Device_Name", Device_Name);
            startActivity(intent);
            }
        });
    }

}
