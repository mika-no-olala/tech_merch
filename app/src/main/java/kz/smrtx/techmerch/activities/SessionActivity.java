package kz.smrtx.techmerch.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
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
        OutletInformationFragment.FragmentListener, OperationsOnOutletFragment.FragmentListener{

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
        String code = Ius.generateUniqueCode(this, "s");
        Ius.writeSharedPreferences(this, Ius.LAST_SESSION_CODE, code);
        session.setSES_CODE(code);
        session.setSES_STARTED(dateStarted);
        session.setSES_USE_ID(Integer.parseInt(Ius.readSharedPreferences(this, Ius.USER_ID)));
        sessionViewModel.insert(session);
    }

    public void openOperations() {
        OperationsFragment operationsFragment = OperationsFragment.getInstance(dateStarted);
        getSupportFragmentManager().beginTransaction().replace(R.id.containerSession, operationsFragment)
                .addToBackStack(null).commit();
    }

    public void openFragment(Fragment fragment, boolean scrollUp) {
        Fragment previous = getSupportFragmentManager().findFragmentById(R.id.containerSession);

        getSupportFragmentManager().beginTransaction().hide(previous).add(R.id.containerSession, fragment)
                .addToBackStack(null).commit();
        if (scrollUp)
            scrollView.post(() -> scrollView.fullScroll(View.FOCUS_UP));
    }

    public void openActivityCreateRequest() {
        Intent intent = new Intent(this, CreateRequestActivity.class);
        startActivity(intent);
    }

    public void openActivityStatuses() {
        Intent intent = new Intent(this, StatusesActivity.class);
        startActivity(intent);
    }

    public void openActivitySync() {
        Intent intent = new Intent(this, SyncActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fragmentIndex = getSupportFragmentManager().getBackStackEntryCount();
        if (fragmentIndex<1) {
            openDialog();
            openOperations();
        }
        else {
//            Log.e("ssssize", pageNames.toString());
//            Log.e("sssse", String.valueOf(fragmentIndex));
            pageNames.remove(fragmentIndex);
            getSupportFragmentManager().popBackStack(fragmentIndex - 1, 0);
            pageName.setText(pageNames.get(fragmentIndex - 1));
        }
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
                session.setSES_FINISHED(Ius.getDateByFormat(new Date(), "dd.MM.yyyy HH:mm:ss"));
                sessionViewModel.update(session);
                dialog.cancel();
                finish();
                openActivitySync();
            }
        });
    }
}