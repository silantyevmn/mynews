package silantyevmn.ru.mynews.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import silantyevmn.ru.mynews.R;
import silantyevmn.ru.mynews.model.entity.Articles;
import silantyevmn.ru.mynews.model.image.ImageLoader;
import silantyevmn.ru.mynews.utils.DateManager;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private IAdapter presenter;
    private ImageLoader imageLoader;

    public RecyclerAdapter(IAdapter presenter, ImageLoader imageLoader) {
        this.presenter = presenter;
        this.imageLoader = imageLoader;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(presenter.getArticlesList().get(position));
    }

    @Override
    public int getItemCount() {
        return presenter.getArticlesList().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView title;
        private ImageView image;
        private ImageView imageSmall;
        private TextView publishedAt;

        MyViewHolder(final View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.home_card_image);
            title = itemView.findViewById(R.id.home_card_title);
            name = itemView.findViewById(R.id.home_card_name);
            imageSmall = itemView.findViewById(R.id.home_card_image_small);
            publishedAt = itemView.findViewById(R.id.home_card_publishedAt);
        }

        public void bind(Articles articles) {
            title.setText(articles.getTitle());
            name.setText(articles.getSource().getName());
            publishedAt.setText(DateManager.getTimePublished(articles.getPublishedAt()));

            imageLoader.loadIconTitle(articles.getUrl(),imageSmall);
            imageLoader.loadIcon(articles.getUrl(),articles.getUrlToImage(),image);

            itemView.setOnClickListener(click->{
                presenter.startWebView(articles);
            });

        }

    }
}

