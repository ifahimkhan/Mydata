package com.android.fahim.livewallpaper.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.fahim.livewallpaper.Adapter.MyRecyclerAdapter;
import com.android.fahim.livewallpaper.Database.DataSource.RecentRepository;
import com.android.fahim.livewallpaper.Database.LocalDatabase.LocalDatabase;
import com.android.fahim.livewallpaper.Database.LocalDatabase.RecentDataSource;
import com.android.fahim.livewallpaper.Database.Recents;
import com.android.fahim.livewallpaper.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class RecentsFragment extends Fragment {

    private static RecentsFragment instance = null;

    RecyclerView recyclerView;
    List<Recents> recentsList;
    MyRecyclerAdapter myRecyclerAdapter;
    Context context;

    CompositeDisposable compositeDisposable;
    RecentRepository recentRepository;


    @SuppressLint("ValidFragment")
    public RecentsFragment(Context context) {
        // Required empty public constructor
        this.context = context;
        compositeDisposable = new CompositeDisposable();
        LocalDatabase localDatabase = LocalDatabase.getInstance(this.context);
        recentRepository = RecentRepository.getInstance(RecentDataSource.getInstance(localDatabase.recentsDAO()));

    }

    public static RecentsFragment getInstance(Context context) {
        if (instance == null)
            instance = new RecentsFragment(context);
        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recents, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_recent);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        recentsList = new ArrayList<>();
        loadRecent();
        myRecyclerAdapter = new MyRecyclerAdapter(context, recentsList);
        recyclerView.setAdapter(myRecyclerAdapter);


        return view;
    }

    private void loadRecent() {

        Disposable disposable = recentRepository.getAllRecents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        new Consumer<List<Recents>>() {
                            @Override
                            public void accept(List<Recents> recents) throws Exception {
                                onGetALLRecentSuccess(recents);
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        }
                );
        compositeDisposable.add(disposable);
    }

    private void onGetALLRecentSuccess(List<Recents> recents) {
        recentsList.clear();
        recentsList.addAll(recents);
        myRecyclerAdapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
