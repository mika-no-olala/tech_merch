package kz.smrtx.techmerch.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import kz.smrtx.techmerch.Ius;
import kz.smrtx.techmerch.R;
import kz.smrtx.techmerch.adapters.CardAdapterImages;
import kz.smrtx.techmerch.adapters.CardAdapterNotes;
import kz.smrtx.techmerch.fragments.OperationsFragment;
import kz.smrtx.techmerch.fragments.RCAddressFragment;
import kz.smrtx.techmerch.fragments.RCEndingFragment;
import kz.smrtx.techmerch.fragments.RCEquipmentFragment;
import kz.smrtx.techmerch.fragments.RCReplaceFragment;
import kz.smrtx.techmerch.fragments.RCSummaryFragment;
import kz.smrtx.techmerch.fragments.RCTypeFragment;
import kz.smrtx.techmerch.fragments.RCWorkFragment;
import kz.smrtx.techmerch.fragments.RCWorkSubtypeFragment;
import kz.smrtx.techmerch.items.RequestPages;
import kz.smrtx.techmerch.items.entities.Note;
import kz.smrtx.techmerch.items.entities.Photo;
import kz.smrtx.techmerch.items.entities.Request;
import kz.smrtx.techmerch.items.entities.Visit;
import kz.smrtx.techmerch.items.viewmodels.ElementViewModel;
import kz.smrtx.techmerch.items.viewmodels.PhotoViewModel;
import kz.smrtx.techmerch.items.viewmodels.RequestViewModel;
import kz.smrtx.techmerch.items.viewmodels.VisitViewModel;

public class CreateRequestActivity extends AppCompatActivity {

    private String currentVisitNumber = "";
    private final ArrayList<RequestPages> pages = new ArrayList<>();
    private int pageIndex = 0;
    private VisitViewModel visitViewModel;
    private RequestViewModel requestViewModel;
    private Request request;
    private PhotoViewModel photoViewModel;

    private boolean guarantee = true;
    private boolean repair = false;
    private boolean replace = false;
    private boolean fromOutToOut = false;
    private boolean responsibleChosen = false;
    private static final String REPAIR = "Ремонт";
    private static final String REPLACE = "Перемещение";
    private ArrayList<String> work = new ArrayList<>();
    private List<Photo> photoList = new ArrayList<>();

    private TextView percentage;
    private Button next;
    private View cloud;
    private View containerForFragment;

    private TextView createdSummary;
    private TextView deadlineSummary;
    private TextView typeSummary;
    private TextView equipmentTypeSummary;
    private TextView equipmentSubtypeSummary;
    private TextView workSummary;
    private TextView replaceSummary;
    private TextView additionalSummary;
    private TextView addressSummary;
    private TextView workSubtypeSummary;
    private TextView specialSummary;
    private TextView executorSummary;
    private TextView commentSummary;
    private RecyclerView recyclerView;

    private Button sendSummary;
    private View summaryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        currentVisitNumber = Ius.readSharedPreferences(this, Ius.LAST_VISIT_NUMBER);
        Log.i("Opened request", currentVisitNumber);

        visitViewModel = new ViewModelProvider(this).get(VisitViewModel.class);
        requestViewModel = new ViewModelProvider(this).get(RequestViewModel.class);
        photoViewModel = new ViewModelProvider(this).get(PhotoViewModel.class);

        createPagesRoute();

        RCTypeFragment rcTypeFragment = RCTypeFragment.getInstance();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_up, R.anim.exit_to_down, R.anim.enter_from_down, R.anim.exit_to_up)
                .replace(R.id.container, rcTypeFragment)
                .addToBackStack(null).commit();

        View back = findViewById(R.id.back);
        next = findViewById(R.id.next);
        percentage = findViewById(R.id.percentage);
        cloud = findViewById(R.id.cloud);
        containerForFragment = findViewById(R.id.container);

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);

        setPercentage(pageIndex);

        initializeVisit();
        initializeSummaryStuff();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!pages.get(pageIndex).isYouCanGoNext()) {
                    createToast(getResources().getString(R.string.fill_field), false);
                    return;
                }

                if (pageIndex==6 && !responsibleChosen) {
                    createToast(getResources().getString(R.string.executor_error), false);
                    return;
                }

                if(pageIndex==6) {
                    makeSummaryFragment();
                }

                route();
                Log.i("NextClicked", "now it is page #" + pageIndex);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("BackClicked", "it was page #" + pageIndex);
                if (pageIndex==0)
                    finish();
                if (containerForFragment.getVisibility() == View.GONE)
                    hideSummary();

                pages.get(pageIndex).setYouCanGoNext(false);
                deleteDataAfterGettingBack();

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

        request.setREQ_CODE(code);
        request.setREQ_SAL_CODE(visit.getVIS_SAL_CODE());
        request.setREQ_CREATED(created);
        request.setREQ_DEADLINE(deadline);
        request.setREQ_UPDATED(created);
        request.setREQ_STA_ID(2);
        request.setREQ_HISTORY_CODE(Ius.generateUniqueCode(this, "h"));
        request.setREQ_TYPE("Гарантированная");
        request.setREQ_USE_CODE(Integer.parseInt(Ius.readSharedPreferences(this, Ius.USER_CODE)));
        request.setREQ_USE_NAME(Ius.readSharedPreferences(this, Ius.USER_NAME));
        request.setREQ_VIS_NUMBER(visit.getVIS_NUMBER());
        request.setNES_TO_UPDATE("yes");
    }

    private void initializeVisit() {
        visitViewModel.getVisitByNumber(currentVisitNumber).observe(this, v -> {
            if (v==null) {
                Log.e("CreateRequest", "cannot find visit");
                next.setEnabled(false);
                return;
            }
            Log.i("InitVisit", "salePointCode - " + v.getVIS_SAL_CODE());
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

    private void initializeSummaryStuff() {
        createdSummary = findViewById(R.id.created);
        deadlineSummary = findViewById(R.id.deadline);
        typeSummary = findViewById(R.id.type);
        equipmentTypeSummary = findViewById(R.id.equipmentType);
        equipmentSubtypeSummary = findViewById(R.id.equipmentSubtype);
        workSummary = findViewById(R.id.work);
        replaceSummary = findViewById(R.id.replace);
        additionalSummary = findViewById(R.id.additional);
        addressSummary = findViewById(R.id.address);
        workSubtypeSummary = findViewById(R.id.workSubtype);
        specialSummary = findViewById(R.id.special);
        executorSummary = findViewById(R.id.executor);
        commentSummary = findViewById(R.id.comment);
        recyclerView = findViewById(R.id.recyclerView);
        sendSummary = findViewById(R.id.send);
        summaryView = findViewById(R.id.summary);

        sendSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestViewModel.insert(request);
                createToast(getResources().getString(R.string.request_success), true);
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void makeSummaryFragment() {
        setAdapter();
        cloud.animate().translationY(cloud.getHeight()).setDuration(300);
        cloud.setVisibility(View.GONE);

        next.animate().translationY(next.getHeight()).setDuration(300);
        next.setVisibility(View.GONE);

        containerForFragment.setVisibility(View.GONE);

        summaryView.animate().translationY(0).setDuration(300);
        summaryView.setVisibility(View.VISIBLE);

        // make formatted
        Date createdDate = Ius.getDateFromString(request.getREQ_CREATED(), "dd.MM.yyyy");
        Date deadlineDate = Ius.getDateFromString(request.getREQ_DEADLINE(), "dd.MM.yyyy");
        createdSummary.setText(Ius.makeTextBold(this, getResources().getString(R.string.date_of_creation) + ": " +
                Ius.getDateByFormat(createdDate, "dd.MM.yyyy, EEEE")));
        deadlineSummary.setText(Ius.makeTextBold(this, getResources().getString(R.string.deadline) + ": " +
                Ius.getDateByFormat(deadlineDate, "dd.MM.yyyy, EEEE")));
        typeSummary.setText(Ius.makeTextBold(this, getResources().getString(R.string.request_type) + ": " + request.getREQ_TYPE()));

        if (isNull(request.getREQ_EQUIPMENT())) {
            equipmentTypeSummary.setVisibility(View.GONE);
            equipmentSubtypeSummary.setVisibility(View.GONE);
        }
        else {
            equipmentTypeSummary.setText(Ius.makeTextBold(this, getResources().getString(R.string.equipment_type) + ": " + request.getREQ_EQUIPMENT()));
            equipmentSubtypeSummary.setText(Ius.makeTextBold(this, getResources().getString(R.string.equipment_subtype) + ": " + request.getREQ_EQU_SUBTYPE()));
            equipmentTypeSummary.setVisibility(View.VISIBLE);
            equipmentSubtypeSummary.setVisibility(View.VISIBLE);
        }

        workSummary.setText(Ius.makeTextBold(this, getResources().getString(R.string.work_type) + ": " + request.getREQ_WORK()));

        if (isNull(request.getREQ_ADDITIONAL()))
            additionalSummary.setVisibility(View.GONE);
        else {
            Log.e("sss", request.getREQ_ADDITIONAL());
            additionalSummary.setText(Ius.makeTextBold(this, getResources().getString(R.string.additional) + ": " + request.getREQ_ADDITIONAL()));
            additionalSummary.setVisibility(View.VISIBLE);
        }

        if (isNull(request.getREQ_REPLACE()))
            replaceSummary.setVisibility(View.GONE);
        else {
            replaceSummary.setText(Ius.makeTextBold(this, getResources().getString(R.string.replace) + ": " + request.getREQ_REPLACE()));
            replaceSummary.setVisibility(View.VISIBLE);
        }

        if (isNull(request.getREQ_ADDRESS_SALEPOINT()))
            addressSummary.setVisibility(View.GONE);
        else {
            addressSummary.setText(Ius.makeTextBold(this, getResources().getString(R.string.address) + ": " + request.getREQ_ADDRESS_SALEPOINT()));
            addressSummary.setVisibility(View.VISIBLE);
        }

        if (isNull(request.getREQ_WORK_SUBTYPE()))
            workSubtypeSummary.setVisibility(View.GONE);
        else {
            workSubtypeSummary.setText(Ius.makeTextBold(this, getResources().getString(R.string.work_subtype) + ": " + request.getREQ_WORK_SUBTYPE()));
            workSubtypeSummary.setVisibility(View.VISIBLE);
        }

        if (isNull(request.getREQ_WORK_SPECIAL()))
            specialSummary.setVisibility(View.GONE);
        else {
            specialSummary.setText(Ius.makeTextBold(this, getResources().getString(R.string.glo_equipment) + ": " + request.getREQ_WORK_SPECIAL()));
            specialSummary.setVisibility(View.VISIBLE);
        }

        executorSummary.setText(Ius.makeTextBold(this, getResources().getString(R.string.executor) + ": " + request.getREQ_USE_NAME_APPOINTED()));

        if (isNull(request.getREQ_COMMENT()))
            commentSummary.setVisibility(View.GONE);
        else {
            commentSummary.setText(Ius.makeTextBold(this, getResources().getString(R.string.comment) + ": " + request.getREQ_COMMENT()));
            commentSummary.setVisibility(View.VISIBLE);
        }
    }

    private void hideSummary() {
        summaryView.animate().translationY(summaryView.getHeight()).setDuration(300);
        summaryView.setVisibility(View.GONE);

        cloud.animate().translationY(0).setDuration(300);
        cloud.setVisibility(View.VISIBLE);

        containerForFragment.setVisibility(View.VISIBLE);

        next.animate().translationY(0).setDuration(300);
        next.setVisibility(View.VISIBLE);
    }

    private void setAdapter() {
        RecyclerView.LayoutManager layoutManager;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        if (photoList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            return;
        }

        CardAdapterImages cardAdapter = new CardAdapterImages(photoList, this);
        recyclerView.setAdapter(cardAdapter);
        cardAdapter.setOnItemClickListener(position -> {
            createDialog(photoList.get(position).getREP_PHOTO());
        });
    }

    private void createDialog(String photoName) {
        Dialog dialog = Ius.createDialog(this, R.layout.dialog_window_image, "");
        ImageView image = dialog.findViewById(R.id.image);

        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + photoName);

        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            image.setImageBitmap(bitmap);
        }

        dialog.show();
    }

    private boolean isNull(String text) {
        if (text==null)
            return true;
        return text.length() == 0;
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
            case 6:
                openFragment(RCSummaryFragment.getInstance(), 7);
                break;
        }
    }

    private void deleteDataAfterGettingBack() {
        switch (pageIndex) {
            case 0:
                break;

            case 1:
                request.setREQ_EQUIPMENT(null);
                request.setREQ_EQU_SUBTYPE(null);
                break;

            case 2:
                request.setREQ_WORK(null);
                request.setREQ_ADDITIONAL(null);
                break;

            case 3:
                request.setREQ_REPLACE(null);
                break;

            case 4:
                request.setREQ_ADDRESS_SALEPOINT(null);
                break;

            case 5:
                request.setREQ_WORK_SUBTYPE(null);
                request.setREQ_WORK_SPECIAL(null);
                break;
            case 6:
                request.setREQ_USE_NAME_APPOINTED(null);
                request.setREQ_USE_CODE_APPOINTED(0);
                photoViewModel.deleteRequestPhotos(request.getREQ_CODE());
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
        pages.add(new RequestPages(0, "type", 0, true, true));
        pages.add(new RequestPages(1, "equipment", 14, false, false));
        pages.add(new RequestPages(2, "work", 29, false, false));
        pages.add(new RequestPages(3, "replace", 43, false, false));
        pages.add(new RequestPages(4, "address", 57, false, false));
        pages.add(new RequestPages(5, "workSubtype", 72, false, false));
        pages.add(new RequestPages(6, "photoComment", 86, false, false));
        pages.add(new RequestPages(7, "result", 100, false, false));
    }

    private String workToString() {
        String workStr = work.toString().replace("[", "");
        workStr = workStr.replace("]", "");
        return workStr;
    }

    private void createToast(String text, boolean success) {
        View layout = getLayoutInflater().inflate(R.layout.toast_window, (ViewGroup) findViewById(R.id.toast));
        Ius.showToast(layout, this, text, success);
    }

    public void setType(boolean guarantee) {
        this.guarantee = guarantee;
        if (guarantee)
            request.setREQ_TYPE("Гарантийная");
        else
            request.setREQ_TYPE("Негарантийная");
    }

    public void setEquipment(String equipment) {
        request.setREQ_EQUIPMENT(equipment);
    }

    public void setEquipmentSubtype(String equipmentSubtype) {
        pages.get(1).setYouCanGoNext(equipmentSubtype.length() > 0);
        request.setREQ_EQU_SUBTYPE(equipmentSubtype);
    }

    public void setRepair(boolean repair) {
        this.repair = repair;
        if (repair)
            work.add(REPAIR);
        else
            work.remove(REPAIR);

        pages.get(2).setYouCanGoNext(work.size()>0); // let it be 2, not 0, don't want to test anything
        request.setREQ_WORK(workToString());
    }

    public void setReplace(boolean replace) {
        this.replace = replace;
        if (replace)
            work.add(REPLACE);
        else
            work.remove(REPLACE);

        pages.get(2).setYouCanGoNext(workToString().length()>2);
        request.setREQ_WORK(workToString());
    }

    public void clearAdditional() {
        request.setREQ_ADDITIONAL(null);
    }

    public void setAdditional(String additional) {
        request.setREQ_ADDITIONAL(additional);
    }

    public void setFromOutToOut(boolean fromOutToOut) {
        this.fromOutToOut = fromOutToOut;
    }
    
    public void setReplacePoint(String replaceChoice) {
        pages.get(3).setYouCanGoNext(replaceChoice.length()>1);
        request.setREQ_REPLACE(replaceChoice);
    }

    public void setAddress(String address) {
        pages.get(4).setYouCanGoNext(address.length()>1);
        request.setREQ_ADDRESS_SALEPOINT(address);
    }

    public void setWorkSubtype(String workSubtype, boolean gloChosen) {
        if (!gloChosen)
            pages.get(5).setYouCanGoNext(workSubtype.length()>1);
        request.setREQ_WORK_SUBTYPE(workSubtype);
    }

    public void setSpecial(String special) {
        pages.get(5).setYouCanGoNext(special.length()>1);
        request.setREQ_WORK_SPECIAL(special);
    }

    public void setExecutor(String code, boolean correct) {
        if(!correct) {
            responsibleChosen = false;
            return;
        }
        String piece = code.substring(0, code.indexOf("-")-1);
        String pieceName = code.substring(code.indexOf("-")+2);
        Log.e("sss name", pieceName);
        try {
            int codeInt = Integer.parseInt(piece);
            request.setREQ_USE_CODE_APPOINTED(codeInt);
            request.setREQ_USE_NAME_APPOINTED(pieceName);
            responsibleChosen = true;
        } catch (Exception e) {
            createToast(getResources().getString(R.string.executor_error), false);
        }
    }

    public void setComment(String comment) {
        pages.get(6).setYouCanGoNext(comment.length()>1);
        request.setREQ_COMMENT(comment);
    }

    public void setPhotos(String[] photos) {
        Log.i("setPhotos", Arrays.toString(photos));
        String userCode = String.valueOf(request.getREQ_USE_CODE());
        int roleCode = Integer.parseInt(Ius.readSharedPreferences(this, Ius.USER_ROLE_CODE));

        photoList.clear();

        for (String s : photos) {
            if (s==null)
                continue;

            if (s.length()>0) {
                photoList.add(new Photo(s, request.getREQ_CODE(), s + ".jpg", userCode, roleCode, "yes"));
            }
        }

        if (photoList.size()>0)
            photoViewModel.insertPhotos(photoList);
    }

    public boolean checkPermissions() {
        return Ius.checkPermissionsCamera(this);
    }
}