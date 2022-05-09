package com.jacobtread.kme.blaze

import com.jacobtread.kme.blaze.PacketComponent.*

@Suppress("unused")
enum class PacketCommand(val component: PacketComponent, val value: Int) {
    //Authentication Component
    UPDATE_ACCOUNT(AUTHENTICATION, 0x14),
    UPDATE_PARENTAL_EMAIL(AUTHENTICATION, 0x1C),
    LIST_USER_ENTITLEMENTS_2(AUTHENTICATION, 0x1D),
    GET_ACCOUNT(AUTHENTICATION, 0x1E),
    GRANT_ENTITLEMENT(AUTHENTICATION, 0x1F),
    LIST_ENTITLEMENTS(AUTHENTICATION, 0x20),
    HAS_ENTITLEMENT(AUTHENTICATION, 0x21),
    GET_USE_COUNT(AUTHENTICATION, 0x22),
    DECREMENT_USE_COUNT(AUTHENTICATION, 0x23),
    GET_AUTH_TOKEN(AUTHENTICATION, 0x24),
    GET_HANDOFF_TOKEN(AUTHENTICATION, 0x25),
    GET_PASSWORD_RULES(AUTHENTICATION, 0x26),
    GRANT_ENTITLEMENT_2(AUTHENTICATION, 0x27),
    LOGIN(AUTHENTICATION, 0x28),
    ACCEPT_TOS(AUTHENTICATION, 0x29),
    GET_TOS_INFO(AUTHENTICATION, 0x2A),
    MODIFY_ENTITLEMENT_2(AUTHENTICATION, 0x2B),
    CONSUME_CODE(AUTHENTICATION, 0x2C),
    PASSWORD_FORGOT(AUTHENTICATION, 0x2D),
    GET_TOS_CONTENT(AUTHENTICATION, 0x2E),
    GET_PRIVACY_POLICY_CONTENT(AUTHENTICATION, 0x2F),
    LIST_PERSONA_ENTITLEMENTS_2(AUTHENTICATION, 0x30),
    SILENT_LOGIN(AUTHENTICATION, 0x32),
    CHECK_AGE_REQUIREMENT(AUTHENTICATION, 0x33),
    GET_OPT_IN(AUTHENTICATION, 0x34),
    ENABLE_OPT_IN(AUTHENTICATION, 0x35),
    DISABLE_OPT_IN(AUTHENTICATION, 0x36),
    EXPRESS_LOGIN(AUTHENTICATION, 0x3C),
    LOGOUT(AUTHENTICATION, 0x46),
    CREATE_PERSONA(AUTHENTICATION, 0x50),
    GET_PERSONA(AUTHENTICATION, 0x5A),
    LIST_PERSONAS(AUTHENTICATION, 0x64),
    LOGIN_PERSONA(AUTHENTICATION, 0x6E),
    LOGOUT_PERSONA(AUTHENTICATION, 0x78),
    DELETE_PERSONA(AUTHENTICATION, 0x8C),
    DISABLE_PERSONA(AUTHENTICATION, 0x8D),
    LIST_DEVICE_ACCOUNTS(AUTHENTICATION, 0x8F),
    XBOX_CREATE_ACCOUNT(AUTHENTICATION, 0x96),
    ORIGIN_LOGIN(AUTHENTICATION, 0x98),
    XBOX_ASSOCIATE_ACCOUNT(AUTHENTICATION, 0xA0),
    XBOX_LOGIN(AUTHENTICATION, 0xAA),
    PS3_CREATE_ACCOUNT(AUTHENTICATION, 0xB4),
    PS3_ASSOCIATE_ACCOUNT(AUTHENTICATION, 0xBE),
    PS3_LOGIN(AUTHENTICATION, 0xC8),
    VALIDATE_SESSION_KEY(AUTHENTICATION, 0xD2),
    CREATE_WAL_USER_SESSION(AUTHENTICATION, 0xE6),
    ACCEPT_LEGAL_DOCS(AUTHENTICATION, 0xF1),
    GET_LEGAL_DOCS_INFO(AUTHENTICATION, 0xF2),
    GET_TERMS_OF_SERVICE_CONTENT(AUTHENTICATION, 0xF6),
    DEVICE_LOGIN_GUEST(AUTHENTICATION, 0x12C),
    CREATE_ACCOUNT(AUTHENTICATION, 0xA),

    // Game Manager Component
    CREATE_GAME(GAME_MANAGER, 0x1),
    DESTROY_GAME(GAME_MANAGER, 0x2),
    ADVANCE_GAME_STATE(GAME_MANAGER, 0x3),
    SET_GAME_SETTINGS(GAME_MANAGER, 0x4),
    SET_PLAYER_CAPACITY(GAME_MANAGER, 0x5),
    SET_PRESENCE_MODE(GAME_MANAGER, 0x6),
    SET_GAME_ATTRIBUTES(GAME_MANAGER, 0x7),
    SET_PLAYER_ATTRIBUTES(GAME_MANAGER, 0x8),
    JOIN_GAME(GAME_MANAGER, 0x9),
    REMOVE_PLAYER(GAME_MANAGER, 0xB),
    START_MATCHMAKING(GAME_MANAGER, 0xD),
    CANCEL_MATCHMAKING(GAME_MANAGER, 0xE),
    FINALIZE_GAME_CREATION(GAME_MANAGER, 0xF),
    LIST_GAMES(GAME_MANAGER, 0x11),
    SET_PLAYER_CUSTOM_DATA(GAME_MANAGER, 0x12),
    REPLAY_GAME(GAME_MANAGER, 0x13),
    RETURN_DEDICATED_SERVER_TO_POOL(GAME_MANAGER, 0x14),
    JOIN_GAME_BY_GROUP(GAME_MANAGER, 0x15),
    LEAVE_GAME_BY_GROUP(GAME_MANAGER, 0x16),
    MIGRATE_GAME(GAME_MANAGER, 0x17),
    UPDATE_GAME_HOST_MIGRATION_STATUS(GAME_MANAGER, 0x18),
    RESET_DEDICATED_SERVER(GAME_MANAGER, 0x19),
    UPDATE_GAME_SESSION(GAME_MANAGER, 0x1A),
    BAN_PLAYER(GAME_MANAGER, 0x1B),
    UPDATE_MESH_CONNECTION(GAME_MANAGER, 0x1D),
    REMOVE_PLAYER_FROM_BANNED_LIST(GAME_MANAGER, 0x1F),
    CLEAR_BANNED_LIST(GAME_MANAGER, 0x20),
    GET_BANNED_LIST(GAME_MANAGER, 0x21),
    ADD_QUEUED_PLAYER_TO_GAME(GAME_MANAGER, 0x26),
    UPDATE_GAME_NAME(GAME_MANAGER, 0x27),
    EJECT_HOST(GAME_MANAGER, 0x28),
    NOTIFY_GAME_UPDATED(GAME_MANAGER, 0x50),
    GET_GAME_LIST_SNAPSHOT(GAME_MANAGER, 0x64),
    GET_GAME_LIST_SUBSCRIPTION(GAME_MANAGER, 0x65),
    DESTROY_GAME_LIST(GAME_MANAGER, 0x66),
    GET_FULL_GAME_DATA(GAME_MANAGER, 0x67),
    GET_MATCH_MAKING_CONFIG(GAME_MANAGER, 0x68),
    GET_GAME_DATA_FROM_ID(GAME_MANAGER, 0x69),
    ADD_ADMIN_PLAYER(GAME_MANAGER, 0x6A),
    REMOVE_ADMIN_PLAYER(GAME_MANAGER, 0x6B),
    SET_PLAYER_TEAM(GAME_MANAGER, 0x6C),
    CHANGE_GAME_TEAM_ID(GAME_MANAGER, 0x6D),
    MIGRATE_ADMIN_PLAYER(GAME_MANAGER, 0x6E),
    GET_USER_SET_GAME_LIST_SUBSCRIPTION(GAME_MANAGER, 0x6F),
    SWAP_PLAYERS_TEAM(GAME_MANAGER, 0x70),
    REGISTER_DYNAMIC_DEDICATED_SERVER_CREATOR(GAME_MANAGER, 0x96),
    UNREGISTER_DYNAMIC_DEDICATED_SERVER_CREATOR(GAME_MANAGER, 0x97),

    // Redirector Component
    GET_SERVER_INSTANCE(REDIRECTOR, 0x1),

    // Stats Component
    GET_STAT_DESCS(STATS, 0x1),
    GET_STATS(STATS, 0x2),
    GET_STAT_GROUP_LIST(STATS, 0x3),
    GET_STAT_GROUP(STATS, 0x4),
    GET_STATS_BY_GROUP(STATS, 0x5),
    GET_DATE_RANGE(STATS, 0x6),
    GET_ENTITY_COUNT(STATS, 0x7),
    GET_LEADERBOARD_GROUP(STATS, 0xA),
    GET_LEADERBOARD_FOLDER_GROUP(STATS, 0xB),
    GET_LEADERBOARD(STATS, 0xC),
    GET_CENTERED_LEADERBOARD(STATS, 0xD),
    GET_FILTERED_LEADERBOARD(STATS, 0xE),
    GET_KEY_SCOPES_MAP(STATS, 0xF),
    GET_STATS_BY_GROUP_ASYNC(STATS, 0x10),
    GET_LEADERBOARD_TREE_ASYNC(STATS, 0x11),
    GET_LEADERBOARD_ENTITY_COUNT(STATS, 0x12),
    GET_STAT_CATEGORY_LIST(STATS, 0x13),
    GET_PERIOD_IDS(STATS, 0x14),
    GET_LEADERBOARD_RAW(STATS, 0x15),
    GET_CENTERED_LEADERBOARD_RAW(STATS, 0x16),
    GET_FILTERED_LEADERBOARD_RAW(STATS, 0x17),
    CHANGE_KEY_SCOPE_VALUE(STATS, 0x18),

    // Util Component
    FETCH_CLIENT_CONFIG(UTIL, 0x1),
    PING(UTIL, 0x2),
    SET_CLIENT_DATA(UTIL, 0x3),
    LOCALIZE_STRINGS(UTIL, 0x4),
    GET_TELEMETRY_SERVER(UTIL, 0x5),
    GET_TICKER_SERVER(UTIL, 0x6),
    PRE_AUTH(UTIL, 0x7),
    POST_AUTH(UTIL, 0x8),
    USER_SETTINGS_LOAD(UTIL, 0xA),
    USER_SETTINGS_SAVE(UTIL, 0xB),
    USER_SETTINGS_LOAD_ALL(UTIL, 0xC),
    DELETE_USER_SETTINGS(UTIL, 0xE),
    FILTER_FOR_PROFANITY(UTIL, 0x14),
    FETCH_QOS_CONFIG(UTIL, 0x15),
    SET_CLIENT_METRICS(UTIL, 0x16),
    SET_CONNECTION_STATE(UTIL, 0x17),
    GET_PSS_CONFIG(UTIL, 0x18),
    GET_USER_OPTIONS(UTIL, 0x19),
    SET_USER_OPTIONS(UTIL, 0x1A),
    SUSPEND_USER_PING(UTIL, 0x1B),

    // Messaging Component
    SEND_MESSAGE(MESSAGING, 0x1),
    FETCH_MESSAGES(MESSAGING, 0x2),
    PURGE_MESSAGES(MESSAGING, 0x3),
    TOUCH_MESSAGES(MESSAGING, 0x4),
    GET_MESSAGES(MESSAGING, 0x5),

    // Association Lists Component
    ADD_USERS_TO_LIST(ASSOCIATION_LISTS, 0x1),
    REMOVE_USERS_FROM_LIST(ASSOCIATION_LISTS, 0x1),
    CLEAR_LISTS(ASSOCIATION_LISTS, 0x3),
    SET_USERS_TO_LIST(ASSOCIATION_LISTS, 0x4),
    GET_LIST_FOR_USER(ASSOCIATION_LISTS, 0x5),
    GET_LISTS(ASSOCIATION_LISTS, 0x6),
    SUBSCRIBE_TO_LISTS(ASSOCIATION_LISTS, 0x7),
    UNSUBSCRIBE_FROM_LISTS(ASSOCIATION_LISTS, 0x8),
    GET_CONFIG_LISTS_INFO(ASSOCIATION_LISTS, 0x9),

    // Game Reporting Component
    SUBMIT_GAME_REPORT(GAME_REPORTING, 0x1),
    SUBMIT_OFFLINE_GAME_REPORT(GAME_REPORTING, 0x2),
    SUBMIT_GAME_EVENTS(GAME_REPORTING, 0x3),
    GET_GAME_REPORT_QUERY(GAME_REPORTING, 0x4),
    GET_GAME_REPORT_QUERIES_LIST(GAME_REPORTING, 0x5),
    GET_GAME_REPORTS(GAME_REPORTING, 0x6),
    GET_GAME_REPORT_VIEW(GAME_REPORTING, 0x7),
    GET_GAME_REPORT_VIEW_INFO(GAME_REPORTING, 0x8),
    GET_GAME_REPORT_VIEW_INFO_LIST(GAME_REPORTING, 0x9),
    GET_GAME_REPORT_TYPES(GAME_REPORTING, 0xA),
    UPDATE_METRIC(GAME_REPORTING, 0xB),
    GET_GAME_REPORT_COLUMN_INFO(GAME_REPORTING, 0xC),
    GET_GAME_REPORT_COLUMN_VALUES(GAME_REPORTING, 0xD),
    SUBMIT_TRUSTED_MID_GAME_REPORT(GAME_REPORTING, 0x64),
    SUBMIT_TRUSTED_END_GAME_REPORT(GAME_REPORTING, 0x65),

    // User Sessions Component
    START_SESSION(USER_SESSIONS, 0x1),
    FETCH_EXTENDED_DATA(USER_SESSIONS, 0x3),
    UPDATE_EXTENDED_DATA_ATTRIBUTE(USER_SESSIONS, 0x5),
    UPDATE_HARDWARE_FLAGS(USER_SESSIONS, 0x8),
    LOOKUP_USER(USER_SESSIONS, 0xC),
    LOOKUP_USERS(USER_SESSIONS, 0xD),
    LOOKUP_USERS_BY_PREFIX(USER_SESSIONS, 0xE),
    UPDATE_NETWORK_INFO(USER_SESSIONS, 0x14),
    LOOKUP_USER_GEO_IP_DATA(USER_SESSIONS, 0x17),
    OVERRIDE_USER_GEO_IP_DATA(USER_SESSIONS, 0x18),
    UPDATE_USER_SESSION_CLIENT_DATA(USER_SESSIONS, 0x19),
    SET_USER_INFO_ATTRIBUTE(USER_SESSIONS, 0x1A),
    RESET_USER_GEO_IP_DATA(USER_SESSIONS, 0x1B),
    LOOKUP_USER_SESSION_ID(USER_SESSIONS, 0x20),
    FETCH_LAST_LOCALE_USED_AND_AUTH_ERROR(USER_SESSIONS, 0x21),
    FETCH_USER_FIRST_LAST_AUTH_TIME(USER_SESSIONS, 0x22),
    RESUME_SESSION(USER_SESSIONS, 0x23),
    UNKNOWN(USER_SESSIONS, -1);


    companion object {
        private val LOOKUP: HashMap<Int, PacketCommand>

        init {
            val values = values()
            LOOKUP = HashMap(values.size)
            var lookupKey: Int
            for (it in values) {
                lookupKey = (it.component.id shl 16) + it.value
                LOOKUP[lookupKey] = it
            }
        }

        fun from(component: Int, command: Int): PacketCommand {
            return LOOKUP[(component shl 16) + command] ?: UNKNOWN
        }
    }
}