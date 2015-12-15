package nz.co.chrisdrake.events.ui.widget;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class FooterAdapter<VH extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter {

    protected static final int VIEW_TYPE_FOOTER = 1;

    private final LayoutInflater inflater;
    private final int footerResId;

    private boolean footerEnabled = false;

    public FooterAdapter(Context context, @LayoutRes int footerResId, boolean footerEnabled) {
        this.inflater = LayoutInflater.from(context);
        this.footerResId = footerResId;
        this.footerEnabled = footerEnabled;
    }

    protected LayoutInflater getInflater() {
        return inflater;
    }

    @Override public int getItemViewType(int position) {
        return isFooterEnabled() && position == (getItemCount() - 1) ? VIEW_TYPE_FOOTER
            : super.getItemViewType(position);
    }

    @Override @CallSuper public int getItemCount() {
        return isFooterEnabled() ? 1 : 0;
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_FOOTER) {
            View v = inflater.inflate(footerResId, parent, false);
            return new FooterViewHolder(v);
        } else {
            return onCreateItemViewHolder(parent, viewType);
        }
    }

    public abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);

    @SuppressWarnings("unchecked") @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) != VIEW_TYPE_FOOTER) {
            onBindItemViewHolder((VH) holder, position);
        }
    }

    public abstract void onBindItemViewHolder(VH holder, int position);

    public boolean isFooterEnabled() {
        return footerEnabled;
    }

    public void setFooterEnabled(boolean footerEnabled) {
        if (footerEnabled != this.footerEnabled) {
            this.footerEnabled = footerEnabled;
            notifyDataSetChanged();
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
