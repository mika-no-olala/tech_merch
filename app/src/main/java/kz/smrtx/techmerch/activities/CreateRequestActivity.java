package kz.smrtx.techmerch.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.fragments.RCAddressFragment;
import kz.smrtx.techmerch.fragments.RCEndingFragment;
import kz.smrtx.techmerch.fragments.RCEquipmentFragment;
import kz.smrtx.techmerch.fragments.RCReplaceFragment;
import kz.smrtx.techmerch.fragments.RCTypeFragment;
import kz.smrtx.techmerch.fragments.RCWorkFragment;
import kz.smrtx.techmerch.fragments.RCWorkSubtypeFragment;
import kz.smrtx.techmerch.items.RequestPages;
import kz.smrtx.techmerch.items.entities.Request;
import kz.smrtx.techmerch.items.entities.Visit;
import kz.smrtx.techmerch.items.viewmodels.ElementViewModel;
import kz.smrtx.techmerch.items.viewmodels.RequestViewModel;
import kz.smrtx.techmerch.items.viewmodels.VisitViewModel;

public class CreateRequestActivity extends AppCompatActivity {

    private String currentVisitNumber = "";
    private final ArrayList<RequestPages> pages = new ArrayList<>();
    private int pageIndex = 0;
    private VisitViewModel visitViewModel;
    private RequestViewModel requestViewModel;
    private Request request;

    private boolean guarantee = true;
    private boolean repair = false;
    private boolean replace = false;
    private boolean fromOutToOut = false;

    private TextView percentage;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        currentVisitNumber = Ius.readSharedPreferences(this, Ius.LAST_VISIT_NUMBER);
        Log.i("Opened request", currentVisitNumber);

        visitViewModel = new ViewModelProvider(this).get(VisitViewModel.class);
        requestViewModel = new ViewModelProvider(this).get(RequestViewModel.class);

        createPagesRoute();

        RCTypeFragment rcTypeFragment = RCTypeFragment.getInstance();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_up, R.anim.exit_to_down, R.anim.enter_from_down, R.anim.exit_to_up)
                .replace(R.id.container, rcTypeFragment)
                .addToBackStack(null).commit();

        View back = findViewById(R.id.back);
        next = findViewById(R.id.next);
        percentage = findViewById(R.id.percentage);

        setPercentage(pageIndex);

        initializeVisit();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                route();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pageIndex==0)
                    finish();

                // removing previous page from active pages
                pages.get(pageIndex).setActive(false);

                int routeBackIndex = routeBack();
                setPercentage(routeBackIndex);
                getSupportFragmentManager().popBackStackImmediate();
            }
        });
    }

    private void createRequest(Visit visit) {
        request = new Request();
        String created = Ius.getDateByFormat(new Date(), "dd.MM.yyyy HH:mm:ss");
        String deadline = Ius.getDateByFormat(Ius.plusDaysToDate(new Date(), 3), "dd.MM.yyyy HH:mm:ss");
        String code = Ius.generateUniqueCode(this, "r");

        Log.e("sss initVisitReq", String.valueOf(visit.getSaleCode()));
        request.setCode(code);
        request.setSalePointCode(visit.getSaleCode());
        request.setCreated(created);
        request.setDeadline(deadline);
        request.setStatusId(1);
        request.setVisitNumber(visit.getNumber());
    }

    private void initializeVisit() {
        visitViewModel.getVisitByNumber(currentVisitNumber).observe(this, v -> {
            if (v==null) {
                Log.e("CreateRequest", "cannot find visit");
                next.setEnabled(false);
                return;
            }
            Log.e("sss initVisit", String.valueOf(v.getSaleCode()));
            createRequest(v);
        });
    }

    public void openFragment(Fragment fragment, int newPageIndex) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_up, R.anim.exit_to_down, R.anim.enter_from_down, R.anim.exit_to_up)
                .hide(f).add(R.id.container, fragment)
                .addToBackStack(null).commit();

        pageIndex = newPageIndex;
        setPercentage(pageIndex);
    }

    @SuppressLint("SetTextI18n")
    private void setPercentage(int page) {
        percentage.setText(getResources().getText(R.string.request_creation) + ": " +
                pages.get(page).getPercentage() + "%");

        pages.get(page).setActive(true);
    }

    private void route() {
        switch (pageIndex) {
            case 0:
                if (guarantee)
                    openFragment(RCWorkFragment.getInstance(), 2);
                else
                    openFragment(RCEquipmentFragment.getInstance(), 1);
                break;

            case 1: openFragment(RCWorkFragment.getInstance(), 2);
                    break;

            case 2:
                if (replace)
                    openFragment(RCReplaceFragment.getInstance(), 3);
                else
                    openFragment(RCEndingFragment.getInstance(), 6);
                break;

            case 3:
                if (fromOutToOut)
                    openFragment(RCAddressFragment.getInstance(), 4);
                else
                    openFragment(RCWorkSubtypeFragment.getInstance(), 5);
                break;

            case 4:
                openFragment(RCWorkSubtypeFragment.getInstance(), 5);
                break;

            case 5:
                openFragment(RCEndingFragment.getInstance(), 6);
                break;
        }
    }

    private int routeBack() {
        int indexBackTo = 0;
        for (int i = pageIndex - 1; i > 0; i--) {
            if (pages.get(i).isActive()) {
                indexBackTo = pages.get(i).getId();
                break;
            }
        }
        pageIndex = indexBackTo;
        return indexBackTo;
    }

    private void createPagesRoute() {
        pages.add(new RequestPages(0, "type", 0, true));
        pages.add(new RequestPages(1, "equipment", 14, false));
        pages.add(new RequestPages(2, "work", 29, false));
        pages.add(new RequestPages(3, "replace", 43, false));
        pages.add(new RequestPages(4, "address", 57, false));
        pages.add(new RequestPages(5, "workSubtype", 72, false));
        pages.add(new RequestPages(6, "photoComment", 86, false));
        pages.add(new RequestPages(7, "result", 100, false));
    }

    public void setType(boolean guarantee) {
        this.guarantee = guarantee;
        if (guarantee)
            request.setType("Гарантийная");
        else
            request.setType("Негарантийная");
    }

    public void setEquipment(String equipment) {
        request.setEquipment(equipment);
    }

    public void setEquipmentSubtype(String equipmentSubtype) {
        request.setEquipmentSubtype(equipmentSubtype);
        Log.e("sss", equipmentSubtype);
    }

    public void setRepair(boolean repair) {
        this.repair = repair;
        request.setWork("Ремонт");
    }

    public void setReplace(boolean replace) {
        this.replace = replace;
        request.setWork("Перемещение");
    }

    public void setFromOutToOut(boolean fromOutToOut) {
        this.fromOutToOut = fromOutToOut;
    }
}