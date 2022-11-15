package edu.skku.cs.groupbuying.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import edu.skku.cs.groupbuying.ItemData;
import edu.skku.cs.groupbuying.MainActivity;
import edu.skku.cs.groupbuying.R;
import edu.skku.cs.groupbuying.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private FragmentDashboardBinding binding;
    private ArrayList<ItemData> mData;
    private ArrayList<ItemData> searchData = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        initDataset();

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.chatroomList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerViewAdapter(mData, (MainActivity) getActivity());
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
        mData.add(new ItemData(R.drawable.ic_baseline_image_24, "채팅방 1"));
    }
}