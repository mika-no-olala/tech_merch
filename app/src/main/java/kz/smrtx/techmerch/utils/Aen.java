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
    public static final int STATUS_WAITING_COORDINATOR = 9;
    public static final int STATUS_COORDINATOR_CANCELED_TO_TMR = 10;
    public static final int STATUS_FINISHED = 5;
}
