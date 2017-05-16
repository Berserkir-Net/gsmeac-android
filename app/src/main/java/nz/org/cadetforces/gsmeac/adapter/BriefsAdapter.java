package nz.org.cadetforces.gsmeac.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import nz.org.cadetforces.gsmeac.R;
import nz.org.cadetforces.gsmeac.adapter.base.BaseRecyclerViewAdapter;
import nz.org.cadetforces.gsmeac.database.model.Brief;

public class BriefsAdapter extends BaseRecyclerViewAdapter {

    private static final int TYPE_BRIEF = 100;

    private BriefClickListener mBriefClickListener;

    public BriefsAdapter(Context context){
        super(context);
    }

    public void setBriefClickListener(BriefClickListener briefClickListener){
        mBriefClickListener = briefClickListener;
    }

    public class BriefViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView createdTextView;

        public BriefViewHolder(View view) {

            super(view);

            nameTextView = (TextView) view.findViewById(R.id.nameTextView);
            createdTextView = (TextView) view.findViewById(R.id.createdTextView);

        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch(viewType){
            case TYPE_BRIEF: {
                return new BriefViewHolder(inflater.inflate(R.layout.item_brief, parent, false));
            }
        }

        return super.onCreateViewHolder(parent, viewType);

    }

    @Override
    public int getItemViewType(int position) {

        Object object = getItem(position);

        if(object instanceof Brief){
            return TYPE_BRIEF;
        }

        return super.getItemViewType(position);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Object object = getItem(position);
        int type = getItemViewType(position);

        switch(type){

            case TYPE_BRIEF: {

                final Brief brief = (Brief) object;
                BriefViewHolder briefViewHolder = (BriefViewHolder) holder;

                String name = brief.getName();
                String created = mContext.getString(R.string.info_last_updated, DateUtils.getRelativeTimeSpanString(brief.getUpdatedAt(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));

                if(TextUtils.isEmpty(name)){
                    name = mContext.getString(R.string.info_no_name);
                }

                briefViewHolder.nameTextView.setText(name);
                briefViewHolder.createdTextView.setText(created);

                briefViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mBriefClickListener != null){
                            mBriefClickListener.onBriefClick(brief);
                        }
                    }
                });

                briefViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if(mBriefClickListener != null){
                            mBriefClickListener.onBriefLongClick(brief);
                            return true;
                        }
                        return false;
                    }
                });

                break;

            }

        }

    }

    public interface BriefClickListener {
        void onBriefClick(Brief brief);
        void onBriefLongClick(Brief brief);
    }

}