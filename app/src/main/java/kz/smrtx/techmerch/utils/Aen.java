package kz.smrtx.techmerch.utils;

public class Aen {
    public static final int ROLE_TMR = 5;
    public static final int ROLE_MANAGER = 6;
    public static final int ROLE_COORDINATOR = 7;
    public static final int ROLE_TECHNIC = 4;

    public static final int STATUS_WAITING_MANAGER = 1;
    public static final int STATUS_WAITING_TECHNIC = 2;
    public static final int STATUS_WAITING_TMR = 3;
    public static final int STATUS_TMR_CANCELED = 4;
    public static final int STATUS_MANAGER_CANCELED = 6;
    public static final int STATUS_TECHNIC_CANCELED_TO_MANAGER = 7;
    public static final int STATUS_TECHNIC_CANCELED_TO_TMR = 8;
    public static final int STATUS_TECHNIC_CANCELED_TO_COORDINATOR = 11;
    public static final int STATUS_WAITING_COORDINATOR = 9;
    public static final int STATUS_COORDINATOR_CANCELED_TO_TMR = 10;
    public static final int STATUS_COORDINATOR_CANCELED_TO_MANAGER = 12;
    public static final int STATUS_COORDINATOR_CANCELED = 13;
    public static final int STATUS_FINISHED = 5;

    public static final int REPLACE_VALUE_TO_STORAGE = 1;
    public static final int REPLACE_VALUE_FROM_STORAGE = 2;
    public static final int REPLACE_VALUE_FROM_OUT_TO_OUT = 3;

    public static int getStatusByExecutorAfterManager(int executorRoleFound) {
        if (executorRoleFound == 7)
            return STATUS_WAITING_COORDINATOR;

        return STATUS_WAITING_TECHNIC;
    }

    public static int getStatusByExecutorAfterTMR(int executorRoleFound) {
        switch (executorRoleFound) {
            case 6:
                return STATUS_WAITING_MANAGER;
            case 7:
                return STATUS_WAITING_COORDINATOR;
            default:
                return STATUS_WAITING_TECHNIC;
        }
    }

    public static int getCancelStatusAfterTechnic(int userRole) {
        switch (userRole) {
            case 6:
                return STATUS_TECHNIC_CANCELED_TO_MANAGER;
            case 7:
                return STATUS_TECHNIC_CANCELED_TO_COORDINATOR;
            default:
                return STATUS_TECHNIC_CANCELED_TO_TMR;
        }
    }
}
