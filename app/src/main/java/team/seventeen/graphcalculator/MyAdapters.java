package team.seventeen.graphcalculator;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import java.util.List;

public class MyAdapters extends BaseAdapter {
    private Context context;
    //    private String[] str;
    private List list;
    public MyAdapters(Context context, List list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_mian, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Bean bean = (Bean) list.get(position);
        Log.e("TAG", viewHolder.text + ":" + position);
        viewHolder.text.setTag(position);
        viewHolder.text.clearFocus();
        viewHolder.text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int pos = (int) viewHolder.text.getTag();
                Bean b  = (Bean) list.get(pos);
                b.setName(s + "");
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        if (!TextUtils.isEmpty(bean.getName())) {
            viewHolder.text.setText(bean.getName());
        } else {
            viewHolder.text.setText("");
        }
        return convertView;
    }
    public class ViewHolder {
        private EditText text;
        public ViewHolder(View v) {
            text = (EditText) v.findViewById(R.id.item);
        }
    }
}