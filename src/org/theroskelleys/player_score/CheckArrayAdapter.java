package org.theroskelleys.player_score;

import java.util.List;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class CheckArrayAdapter extends ArrayAdapter<CheckRowItem>
{
	private final List<CheckRowItem> list;
	private final Activity context;
	
	public CheckArrayAdapter(Activity context, List<CheckRowItem> list)
	{
		super(context, R.layout.player_row, list);
		this.context = context;
		this.list = list;
	}
	
	static class ViewHolder
	{
		protected TextView text;
		protected CheckBox checkbox;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = null;
		if (convertView == null)
		{
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.player_row, null);
			
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) view.findViewById(R.id.label);
			viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
			//TODO figure out how to attach click handler to the view itself, instead of just the checkbox
			viewHolder.checkbox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
												 boolean isChecked)
					{
						CheckRowItem element = (CheckRowItem) viewHolder.checkbox
							.getTag();
						element.setSelected(buttonView.isChecked());
					}
				});
			view.setTag(viewHolder);
			viewHolder.checkbox.setTag(list.get(position));
		}
		else
		{
			view = convertView;
			((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.text.setText(list.get(position).getName());
		holder.checkbox.setChecked(list.get(position).isSelected());
		return view;
	}
}
