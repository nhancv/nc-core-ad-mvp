package com.cvnhan.androidcr.utils.rvsession;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Created by nhancao on 6/17/16.
 */
public class RvSectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SECTION_TYPE = 0;
    private final Context context;
    private boolean valid = true;
    private int sectionResourceId;
    private int textResourceId;
    private RvContentAdapter baseAdapter;
    private SparseArray<Section> sections = new SparseArray<>();


    public RvSectionAdapter(Context context, int sectionResourceId, int textResourceId,
                            RvContentAdapter baseAdapter) {
        this.sectionResourceId = sectionResourceId;
        this.textResourceId = textResourceId;
        this.baseAdapter = baseAdapter;
        this.context = context;

        baseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                valid = baseAdapter.getItemCount() > 0;
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                valid = baseAdapter.getItemCount() > 0;
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                valid = baseAdapter.getItemCount() > 0;
                notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                valid = baseAdapter.getItemCount() > 0;
                notifyItemRangeRemoved(positionStart, itemCount);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int typeView) {
        if (typeView == SECTION_TYPE) {
            final View view = LayoutInflater.from(context).inflate(sectionResourceId, parent, false);
            return new SectionViewHolder(view, textResourceId);
        } else {
            return baseAdapter.onCreateViewHolder(parent, typeView - 1);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder sectionViewHolder, int position) {
        if (isSectionHeaderPosition(position)) {
            ((SectionViewHolder) sectionViewHolder).title.setText(sections.get(position).title);
        } else {
            baseAdapter.onBindViewHolder((RvContentAdapter.SimpleViewHolder) sectionViewHolder, sectionedPositionToPosition(position));
        }

    }

    @Override
    public int getItemViewType(int position) {
        return isSectionHeaderPosition(position)
                ? SECTION_TYPE
                : baseAdapter.getItemViewType(sectionedPositionToPosition(position)) + 1;
    }

    public void setSections(Section[] sections) {
        this.sections.clear();

        Arrays.sort(sections, (o, o1) -> (o.firstPosition == o1.firstPosition)
                ? 0
                : ((o.firstPosition < o1.firstPosition) ? -1 : 1));

        int offset = 0; // offset positions for the headers we're adding
        for (Section section : sections) {
            section.sectionedPosition = section.firstPosition + offset;
            this.sections.append(section.sectionedPosition, section);
            ++offset;
        }

        notifyDataSetChanged();
    }

    public int positionToSectionedPosition(int position) {
        int offset = 0;
        for (int i = 0; i < sections.size(); i++) {
            if (sections.valueAt(i).firstPosition > position) {
                break;
            }
            ++offset;
        }
        return position + offset;
    }

    public int sectionedPositionToPosition(int sectionedPosition) {
        if (isSectionHeaderPosition(sectionedPosition)) {
            return RecyclerView.NO_POSITION;
        }

        int offset = 0;
        for (int i = 0; i < sections.size(); i++) {
            if (sections.valueAt(i).sectionedPosition > sectionedPosition) {
                break;
            }
            --offset;
        }
        return sectionedPosition + offset;
    }

    public boolean isSectionHeaderPosition(int position) {
        return sections.get(position) != null;
    }

    @Override
    public long getItemId(int position) {
        return isSectionHeaderPosition(position)
                ? Integer.MAX_VALUE - sections.indexOfKey(position)
                : baseAdapter.getItemId(sectionedPositionToPosition(position));
    }

    @Override
    public int getItemCount() {
        return (valid ? baseAdapter.getItemCount() + sections.size() : 0);
    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder {

        public TextView title;

        public SectionViewHolder(View view, int mTextResourceid) {
            super(view);
            title = (TextView) view.findViewById(mTextResourceid);
        }
    }

    public static class Section {
        int firstPosition;
        int sectionedPosition;
        CharSequence title;

        public Section(int firstPosition, CharSequence title) {
            this.firstPosition = firstPosition;
            this.title = title;
        }

        public CharSequence getTitle() {
            return title;
        }
    }

}