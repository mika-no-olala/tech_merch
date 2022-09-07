package kz.smrtx.techmerch.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.fragments.OperationsFragment;
import kz.smrtx.techmerch.fragments.OperationsOnOutletFragment;
import kz.smrtx.techmerch.fragments.OutletInformationFragment;
import kz.smrtx.techmerch.fragments.OutletsFragment;

public class SessionActivity extends AppCompatActivity implements OperationsFragment.FragmentListener, OutletsFragment.FragmentListener,
        OutletInformationFragment.FragmentListener, OperationsOnOutletFragment.FragmentListener {

    private TextView pageName;
    private ArrayList<String> pageNames = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        OperationsFragment operationsFragment = OperationsFragment.getInstance("tmr");
        getSupportFragmentManager().beginTransaction().replace(R.id.containerSession, operationsFragment)
                .addToBackStack(null).commit();

        pageName = findViewById(R.id.pageName);
        View back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void getPageName(String name) {
        pageName.setText(name);
        pageNames.add(name);
    }

    public void openOutlets() {
        OutletsFragment outletsFragment = OutletsFragment.getInstance("tmr");
        getSupportFragmentManager().beginTransaction().add(R.id.containerSession, outletsFragment)
                .addToBackStack(null).commit();
    }

    public void openOutletInformation(String outletName) {
        OutletInformationFragment outletInformationFragment = OutletInformationFragment.getInstance("tmr", outletName);
        getSupportFragmentManager().beginTransaction().add(R.id.containerSession, outletInformationFragment)
                .addToBackStack(null).commit();
    }

    public void openOperationsOnOutlet(String outletName) {
        OperationsOnOutletFragment operationsOnOutletFragment = OperationsOnOutletFragment.getInstance("tmr", outletName);
        getSupportFragmentManager().beginTransaction().add(R.id.containerSession, operationsOnOutletFragment)
                .addToBackStack(null).commit();
    }

    public void openActivityCreateRequest() {
        Intent intent = new Intent(this, CreateRequestActivity.class);
        intent.putExtra("OUT_ID", "ул. Фурманова 117");
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        getSupportFragmentManager().popBackStackImmediate("operations", 0);
//        int index = getFragmentManager().getBackStackEntryCount();
//        Log.e("backStackAmount", String.valueOf(getFragmentManager()));
//        FragmentManager.BackStackEntry lastEntry = (FragmentManager.BackStackEntry) getFragmentManager().getBackStackEntryAt(index);
//        FragmentManager.BackStackEntry secondLastEntry = (FragmentManager.BackStackEntry) getFragmentManager().getBackStackEntryAt(index - 1);
//        Log.e(String.valueOf(getSupportFragmentManager().getBackStackEntryCount()), pageNames.get(getSupportFragmentManager().getBackStackEntryCount() - 1));
        int index = getSupportFragmentManager().getBackStackEntryCount();
        if (index!=0) {
            getSupportFragmentManager().popBackStack(index - 1, 0);
            pageName.setText(pageNames.get(index-1));
            pageNames.remove(index);
        }
        else {
            finish();
        }
    }


}