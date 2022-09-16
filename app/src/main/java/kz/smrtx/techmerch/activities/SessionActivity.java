package kz.smrtx.techmerch.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.fragments.OperationsFragment;
import kz.smrtx.techmerch.fragments.OperationsOnOutletFragment;
import kz.smrtx.techmerch.fragments.OutletInformationFragment;
import kz.smrtx.techmerch.fragments.OutletsFragment;
import kz.smrtx.techmerch.items.entities.Session;
import kz.smrtx.techmerch.items.viewmodels.SessionViewModel;

public class SessionActivity extends AppCompatActivity implements OperationsFragment.FragmentListener, OutletsFragment.FragmentListener,
        OutletInformationFragment.FragmentListener, OperationsOnOutletFragment.FragmentListener {

    private TextView pageName;
    private ScrollView scrollView;
    private ArrayList<String> pageNames = new ArrayList<>();
    private String dateStarted;
    private SessionViewModel sessionViewModel;
    private Session session;
    private int fragmentIndex = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        dateStarted = Ius.getDateByFormat(new Date(), "dd.MM.yyyy HH:mm:ss");

        openOperations();

        TextView bottomBarText = findViewById(R.id.bottomBarText);
        scrollView = findViewById(R.id.scrollView);
        pageName = findViewById(R.id.pageName);
        View back = findViewById(R.id.back);
        sessionViewModel = new ViewModelProvider(this).get(SessionViewModel.class);
        bottomBarText.setText(Ius.readSharedPreferences(this, Ius.BOTTOM_BAR_TEXT));

        generateSession();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragmentIndex<1)
                    openDialog();
                else
                    onBackPressed();
            }
        });
    }

    @Override
    public void getPageName(String name) {
        pageName.setText(name);
        pageNames.add(name);
    }

    public void generateSession() {
        session = new Session();
        session.setCode(Ius.generateUniqueCode(this, "s"));
        session.setStarted(dateStarted);
        session.setUserId(Integer.parseInt(Ius.readSharedPreferences(this, Ius.USER_ID)));
        sessionViewModel.insert(session);
    }

    public void openOperations() {
        OperationsFragment operationsFragment = OperationsFragment.getInstance(dateStarted);
        getSupportFragmentManager().beginTransaction().replace(R.id.containerSession, operationsFragment)
                .addToBackStack(null).commit();
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

    public void openOperationsOnOutlet(String outletCode) {
        OperationsOnOutletFragment operationsOnOutletFragment = OperationsOnOutletFragment.getInstance("tmr", outletCode);
        getSupportFragmentManager().beginTransaction().add(R.id.containerSession, operationsOnOutletFragment)
                .addToBackStack(null).commit();
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_UP));
    }

    public void openActivityCreateRequest() {
        Intent intent = new Intent(this, CreateRequestActivity.class);
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
            fragmentIndex = getSupportFragmentManager().getBackStackEntryCount();
            getSupportFragmentManager().popBackStack(fragmentIndex - 1, 0);
            pageName.setText(pageNames.get(fragmentIndex-1));
            pageNames.remove(fragmentIndex);
    }

    public void openDialog() {
        Dialog dialog = Ius.createDialogAcception(this, getResources().getString(R.string.finishing_work),
                getResources().getString(R.string.finishing_work_question), true);

        Button yes = dialog.findViewById(R.id.positive);
        Button no = dialog.findViewById(R.id.negative);

        dialog.show();

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.setFinished(Ius.getDateByFormat(new Date(), "dd.MM.yyyy HH:mm:ss"));
                sessionViewModel.update(session);
                dialog.cancel();
                finish();
            }
        });
    }
}