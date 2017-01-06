package com.example.crunky.testapp;

import android.content.*;
import android.view.*;
import android.widget.*;

import java.net.InetAddress;

/**
 * Created by Daniel on 01.01.2017.
 */

public class BlockFactoryListAdapter extends ArrayAdapter<BlockFactory> {
    public BlockFactoryListAdapter(Context context, BlockFactory[] factories) {
        super(context, R.id.layout_block_factory_list_item, factories);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        InetAddress host=((BlockFactory)(getItem(position))).getHost();
        ( (TextView) (convertView.findViewById(R.id.tvHostname))).setText(host.getHostName());
        ( (TextView) (convertView.findViewById(R.id.tvIp))).setText(host.getHostAddress());
        return convertView;
    }
}
