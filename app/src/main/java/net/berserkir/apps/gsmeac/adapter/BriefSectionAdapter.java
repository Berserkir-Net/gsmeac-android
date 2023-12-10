package net.berserkir.apps.gsmeac.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import net.berserkir.apps.gsmeac.R;
import net.berserkir.apps.gsmeac.adapter.base.BaseRecyclerViewAdapter;

public class BriefSectionAdapter extends BaseRecyclerViewAdapter {

    private static final int TYPE_BRIEF_SECTION = 100;

    public BriefSectionAdapter(Context context){
        super(context);
    }

    public class BriefSectionViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView contentTextView;

        public BriefSectionViewHolder(View view) {

            super(view);

            titleTextView = (TextView) view.findViewById(R.id.titleTextView);
            contentTextView = (TextView) view.findViewById(R.id.contentTextView);

        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch(viewType){
            case TYPE_BRIEF_SECTION: {
                return new BriefSectionViewHolder(inflater.inflate(R.layout.item_brief_section, parent, false));
            }
        }

        return super.onCreateViewHolder(parent, viewType);

    }

    @Override
    public int getItemViewType(int position) {

        Object object = getItem(position);

        if(object instanceof BriefSection){
            return TYPE_BRIEF_SECTION;
        }

        return super.getItemViewType(position);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Object object = getItem(position);
        int type = getItemViewType(position);

        switch(type){

            case TYPE_BRIEF_SECTION: {

                final BriefSection briefSection = (BriefSection) object;
                BriefSectionViewHolder briefSectionViewHolder = (BriefSectionViewHolder) holder;

                briefSectionViewHolder.titleTextView.setText(briefSection.getTitle());
                briefSectionViewHolder.contentTextView.setText(briefSection.getContent());

                break;

            }

        }

    }

    public static class BriefSection {

        private String mTitle;
        private String mContent;

        public BriefSection(String title, String content) {
            mTitle = title;
            mContent = content;
        }

        public String getTitle() {
            return mTitle;
        }

        public String getContent() {
            return mContent;
        }

    }

}