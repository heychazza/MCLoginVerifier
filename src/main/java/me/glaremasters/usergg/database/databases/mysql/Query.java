package me.glaremasters.usergg.database.databases.mysql;

class Query {

    static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `accounts` (\n"
            + "    `uuid` varchar(36) NOT NULL ,\n"
            + "    `token` varchar(10) NOT NULL ,\n"
            + "    PRIMARY KEY (`uuid`), \n"
            + "    UNIQUE (`uuid`)\n"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

    static final String INSERT_USER = "INSERT IGNORE INTO `accounts` (uuid, token) VALUES(?, ?)";

    static final String TOKEN_CHECK = "SELECT uuid from `accounts` WHERE uuid=?";

    static final String UPDATE_TOKEN = "UPDATE accounts set token=? WHERE uuid=?";

}
