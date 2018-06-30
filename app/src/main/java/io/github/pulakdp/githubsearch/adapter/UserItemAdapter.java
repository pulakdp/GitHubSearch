package io.github.pulakdp.githubsearch.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.pulakdp.githubsearch.FullScreenActivity;
import io.github.pulakdp.githubsearch.R;
import io.github.pulakdp.githubsearch.response.Item;

/**
 * Author: PulakDebasish
 */
public class UserItemAdapter extends RecyclerView.Adapter<UserItemAdapter.UserItemViewHolder> {

    private Activity context;
    private List<Item> githubUsers;

    public UserItemAdapter(Activity context, List<Item> githubUsers) {
        this.context = context;
        this.githubUsers = githubUsers;
    }

    @NonNull
    @Override
    public UserItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.user_item_layout, parent, false);
        return new UserItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserItemViewHolder holder, int position) {

        Item githubUser = githubUsers.get(position);

        Glide.with(context)
                .load(githubUser.getAvatarUrl())
                .into(holder.userAvatar);
        holder.username.setText(githubUser.getLogin());
        holder.githubUrl.setText(githubUser.getHtmlUrl());
    }

    @Override
    public int getItemCount() {
        return githubUsers.size();
    }

    public void setData(List<Item> githubUsers) {
        this.githubUsers = githubUsers;
        notifyDataSetChanged();
    }

    public class UserItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.card_view)
        CardView cardView;

        @BindView(R.id.user_avatar)
        ImageView userAvatar;

        @BindView(R.id.username)
        TextView username;

        @BindView(R.id.github_url)
        TextView githubUrl;

        UserItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            userAvatar.setOnClickListener(this);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.user_avatar) {
                Bundle bundle = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(context, userAvatar, userAvatar.getTransitionName()).toBundle();
                }
                context.startActivity(new Intent(context, FullScreenActivity.class)
                        .putExtra("url", githubUsers.get(getAdapterPosition()).getAvatarUrl()), bundle);
                return;
            }
            String urlString = githubUrl.getText().toString();
            if (urlString.isEmpty()) {
                return;
            }
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
            context.startActivity(browserIntent);
        }
    }

}
