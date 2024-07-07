package ir.ac.kntu;

public class OptionEnums {
    public enum UserMainMenuOption {
        ACCOUNT_MANAGEMENT,
        FUNDS_MANAGEMENT,
        CONTACTS,
        TRANSFER_MONEY,
        CHARGE_SIM,
        SUPPORT,
        SETTINGS,
        ACCOUNT_DETAILS,
        LOG_OUT,
        QUIT;
    }

    public enum FundManagementOptions{
        VIEW_FUNDS,
        ADD_NEW_FUND,
        RETURN;
    }

    public enum SelectedFundMenu{
        DEPOSIT,
        WITHDRAW,
        VIEW_BALANCE,
        RETURN;
    }

    public enum ChargeSimOptions{
        YOURSELF,
        CONTACTS,
        PHONE_NUMBER,
        RETURN;
    }

    public enum SupportMenuOption {
        NEW_TICKET,
        TICKETS,
        RETURN;
    }

    public enum SettingsMenuOption {
        CHANGE_PASSWORD,
        CHANGE_CREDIT_CARD_PASSWORD,
        ACTIVATE_DEACTIVATE_CONTACTS,
        RETURN;
    }

    public enum TransferMenuOption {
        ACCOUNT_ID,
        CONTACTS,
        RECENT_USERS,
        RETURN;
    }

    public enum ContactsMenuOption {
        ADD_CONTACT,
        VIEW_CONTACTS,
        RETURN;
    }

    public enum ManagementMenuOption {
        CHARGE,
        VIEW_ACCOUNT_BALANCE,
        VIEW_SIM_CARD_BALANCE,
        VIEW_RECEIPTS,
        RETURN;
    }

    public enum ShowReceiptsOption {
        VIEW_ALL,
        FILTER_BY_TIME_SPAN,
        VIEW_IN_HTML_FILE,
        RETURN;
    }

    public enum SelectRuleOption {
        USER,
        ADMIN,
        MANAGER,
        QUIT;
    }

    public enum ManagerMenu {
        SETTINGS,
        USER_MANAGEMENT,
        AUTO_TRANSACTION,
        LOG_OUT;
    }

    public enum ManagerUserManageMenu {
        VIEW_USERS,
        ADD_MANAGER_OR_ADMIN,
        RETURN;
    }

    public enum ManagerAddMenu {
        MANAGER,
        ADMIN,
        RETURN;
    }

    public enum ManagerViewUserMenu{
        VIEW_ALL,
        VIEW_BY_FILTER,
        RETURN;
    }

    public enum EditUserMenu {
        CHANGE_NAME,
        CHANGE_LASTNAME,
        BLOCK_OR_UNBLOCK,
        RETURN;
    }

    public enum EditManagerMenu {
        CHANGE_NAME,
        BLOCK_OR_UNBLOCK,
        RETURN;
    }

    public enum SearchMethods {
        NAME,
        LASTNAME,
        PHONE_NUMBER,
        COMBINATION,
        RETURN;
    }

    public enum EditAdminMenu {
        CHANGE_NAME,
        BLOCK_OR_UNBLOCK,
        MODIFY_RESPONSIBILITIES,
        RETURN;
    }
    public enum ManagerAutoTransMenu {
        PAYA_TRANSFERS,
        INTEREST_FUNDS,
        RETURN;
    }

    public enum ManagerSettingMenu {
        CHANGE_FEE_RATES,
        CHANGE_INTEREST_RATE,
        RETURN;
    }

    public enum ManagerChangeFee {
        PAYA_FEE,
        POL_FEE,
        FARI_FEE,
        CARD_FEE,
        SIMCARD_FEE,
        RETURN;
    }

    public enum AdminMenu {
        TICKETS,
        USERS,
        LOG_OUT;
    }

    public enum AdminUserMenu {
        VIEW_ALL,
        SEARCH,
        RETURN;
    }

    public enum SignOrLogin {
        LOGIN,
        SIGN_UP,
        RETURN;
    }

    public enum Rules{
        USER,
        MANAGER,
        ADMIN;
    }
}

