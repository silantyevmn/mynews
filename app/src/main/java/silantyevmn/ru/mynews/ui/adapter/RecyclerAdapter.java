package silantyevmn.ru.mynews.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import silantyevmn.ru.mynews.R;
import silantyevmn.ru.mynews.model.entity.Articles;
import silantyevmn.ru.mynews.model.image.ImageLoader;
import silantyevmn.ru.mynews.ui.PopupClass;
import silantyevmn.ru.mynews.utils.DateManager;
import silantyevmn.ru.mynews.utils.Messages;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private IAdapter presenter;
    private ImageLoader imageLoader;
    private PopupClass pop;

    public RecyclerAdapter(IAdapter presenter, ImageLoader imageLoader) {
        this.presenter = presenter;
        this.imageLoader = imageLoader;
        this.pop = new PopupClass();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new MyViewHolder(rootView, pop);
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
        private ImageView customPopupMenu;
        private PopupClass pop;

        MyViewHolder(final View itemView, PopupClass pop) {
            super(itemView);
            image = itemView.findViewById(R.id.home_card_image);
            title = itemView.findViewById(R.id.home_card_title);
            name = itemView.findViewById(R.id.home_card_name);
            imageSmall = itemView.findViewById(R.id.home_card_image_small);
            publishedAt = itemView.findViewById(R.id.home_card_publishedAt);
            customPopupMenu = itemView.findViewById(R.id.home_card_more_vert_action);
            this.pop = pop;
        }

        public void bind(Articles articles) {
            title.setText(articles.getTitle());
            name.setText(articles.getSource().getName());
            publishedAt.setText(DateManager.getTimePublished(articles.getPublishedAt()));

            imageLoader.loadIconTitle(articles.getUrl(), imageSmall);
            imageLoader.loadIcon(articles.getUrl(), articles.getUrlToImage(), image);

            itemView.setOnClickListener(view -> {
                presenter.startWebView(articles);
            });
            customPopupMenu.setOnClickListener(view -> {
                showCustomMenu(view, articles);
            });

        }

        private void showCustomMenu(View view, Articles articles) {
            PopupMenu popupMenu = new PopupMenu(itemView.getContext(), view);
            popupMenu.inflate(R.menu.menu_web);
            MenuItem menuBookmark = popupMenu.getMenu().findItem(R.id.web_menu_bookmark);
            pop.getStatusBookmark(articles, new IBookmark() {
                @Override
                public void onSuccess(boolean isBookmark) {
                    if (isBookmark) {
                        menuBookmark.setTitle(R.string.bookmark_remove);
                        menuBookmark.setIcon(R.drawable.ic_bookmark_black_24dp);
                    } else {
                        menuBookmark.setTitle(R.string.bookmark_add);
                        menuBookmark.setIcon(R.drawable.ic_bookmark_border_black_24dp);
                    }
                }

                @Override
                public void onError(String message) {
                    presenter.showError(message);
                }
            });

            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.web_menu_share: {
                        pop.share(articles);
                        return true;
                    }
                    case R.id.web_menu_bookmark: {
                        pop.updateBookmark(articles, new IBookmark() {
                            @Override
                            public void onSuccess(boolean isBookmark) {
                                if (isBookmark) presenter.showSuccess(Messages.getBookmarkSuccessAdd());
                                else presenter.showSuccess(Messages.getBookmarkSuccessRemove());
                                presenter.updateStatusBookmarks();
                            }

                            @Override
                            public void onError(String message) {
                                presenter.showError(Messages.getErrorUpdateBookmark());
                            }
                        });
                        return true;
                    }
                    default:
                        return false;
                }
            });
            popupMenu.show();
        }

    }
}

