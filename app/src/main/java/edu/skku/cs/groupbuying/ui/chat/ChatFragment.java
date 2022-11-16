package edu.skku.cs.groupbuying.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;

import edu.skku.cs.groupbuying.ItemData;
import edu.skku.cs.groupbuying.MainActivity;
import edu.skku.cs.groupbuying.R;
import edu.skku.cs.groupbuying.databinding.FragmentHomeBinding;

public class ChatFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    //private FragmentChatBinding binding;
    private ArrayList<ItemData> mData;
    private ArrayList<ItemData> searchData = new ArrayList<>();

    /*

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final FloatingActionButton add = binding.addNew;
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                activity.HomeToCreate();
            }
        });

        final Button search = binding.search;
        final EditText search_space = binding.searchSpace;
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchData.clear();
                String input = search_space.getText().toString().toLowerCase().replaceAll("\\s", "");
                for(int i = 0; i < mData.size(); i++){
                    if(mData.get(i).item_title.toLowerCase().replaceAll("\\s", "").contains(input)){
                        searchData.add(mData.get(i));
                    }
                }
                adapter.setItems(searchData);
            }
        });


        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecycleViewAdapter(mData, (MainActivity) getActivity());
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerDecoration =
                new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(getContext()).getOrientation());
        recyclerView.addItemDecoration(dividerDecoration);


        hideBottomNavigation(false);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void hideBottomNavigation(Boolean bool) {
        BottomNavigationView bottomNavigation = getActivity().findViewById(R.id.nav_view);
        if (bool == true)
            bottomNavigation.setVisibility(View.GONE);
        else
            bottomNavigation.setVisibility(View.VISIBLE);
    }

    private void initDataset() {
        ////////////////////////////테스트 중 원래 api 필요
        mData = new ArrayList<ItemData>();
        mData.add(new ItemData(R.drawable.ic_baseline_image_24, "과자 공구"));
        mData.add(new ItemData(R.drawable.ic_baseline_image_24, "콜라 공구"));
        mData.add(new ItemData(R.drawable.ic_baseline_image_24, "당근 공구"));
        mData.add(new ItemData(R.drawable.ic_baseline_image_24, "펩시 공구"));
        mData.add(new ItemData(R.drawable.ic_baseline_image_24, "과잠 공구"));
        mData.add(new ItemData(R.drawable.ic_baseline_image_24, "컴퓨터 공구"));
        mData.add(new ItemData(R.drawable.ic_baseline_image_24, "노트북 공구"));
        mData.add(new ItemData(R.drawable.ic_baseline_image_24, "공동구매"));
    }*/
}