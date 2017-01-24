package com.example.crunky.smartminifab;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.net.*;

import com.example.crunky.smartminifab.IFabCommunication;
import static com.example.crunky.smartminifab.R.id.*;

/**
 * Created by Daniel on 01.01.2017.
 */

public class FabCommunicationListAdapter extends ArrayAdapter<IFabCommunication> {
    public FabCommunicationListAdapter(Context context, IFabCommunication[] factories) {
        super(context, R.id.layout_fab_communication_list_item, factories);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        InetAddress host=((IFabCommunication)(getItem(position))).getHost();
        ( (TextView) (convertView.findViewById(R.id.tvHostname))).setText(host.getHostName());
        ( (TextView) (convertView.findViewById(R.id.tvIp))).setText(host.getHostAddress());
        return convertView;
    }
}