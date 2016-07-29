package com.ideas.sportscounter.exercises.create;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ideas.sportscounter.R;
import com.ideas.sportscounter.core.UserNotifier;
import com.ideas.sportscounter.databinding.VCreateExHeaderBinding;
import com.ideas.sportscounter.databinding.VCreateSetItemBinding;
import com.ideas.sportscounter.exercises.create.viewmodel.CreateSetVM;
import com.ideas.sportscounter.exercises.create.viewmodel.ExerciseHeaderModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Qualifier;

public class ExCreationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ExerciseHeaderModel headerModel;
    private List<CreateSetVM> sets = new LinkedList<>();

    public ExCreationAdapter(Context context, final UserNotifier notifier) {
        final String setTitlePattern = context.getString(R.string.set);
        headerModel = new ExerciseHeaderModel(notifier, new OnItemsAddedListener() {
            @Override
            public void addItems(int setsCount, int times) {
                int prevSize = 0;
                for (int i = 0; i < setsCount; i++) {
                    sets.add(new CreateSetVM(notifier, String.format(setTitlePattern, i + 1), times));
                    notifyItemInserted(sets.size());
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, @ViewType int viewType) {
        if (viewType == ViewType.HEADER) {
            VCreateExHeaderBinding headerBinding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                            R.layout.v_create_ex_header, parent, false);
            return new HeaderViewHolder(headerBinding);
        } else if (viewType == ViewType.CONTENT) {
            VCreateSetItemBinding setBinging = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.v_create_set_item, parent, false);
            return new ContentViewHolder(setBinging);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ViewType.HEADER) {
            ((HeaderViewHolder) holder).bindTo(headerModel);
        } else {
            ((ContentViewHolder) holder).bindTo(sets.get(getPositionForContent(position)));
        }
    }

    public int getPositionForContent(int position) {
        return position - 1;
    }

    @Override
    public int getItemCount() {
        return 1 + sets.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ViewType.HEADER : ViewType.CONTENT;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        VCreateExHeaderBinding headerBinding;

        public HeaderViewHolder(VCreateExHeaderBinding binding) {
            super(binding.getRoot());
            headerBinding = binding;
        }

        public void bindTo(ExerciseHeaderModel model) {
            headerBinding.setModel(model);
            headerBinding.executePendingBindings();
        }

    }

    private static class ContentViewHolder extends RecyclerView.ViewHolder {

        private final VCreateSetItemBinding binding;

        public ContentViewHolder(VCreateSetItemBinding createSetVM) {
            super(createSetVM.getRoot());
            this.binding = createSetVM;
        }

        public void bindTo(CreateSetVM set) {
            binding.setSet(set);
            binding.executePendingBindings();
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Qualifier
    @IntDef({ViewType.HEADER, ViewType.CONTENT})
    @interface ViewType {
        int HEADER = 1;
        int CONTENT = 2;
    }

    public interface OnItemsAddedListener {
        void addItems(int sets, int times);
    }
}
