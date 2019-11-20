package ch.ribeironelson.kargobike.database.pojo;

import java.util.List;

import ch.ribeironelson.kargobike.database.entity.AccountEntity;
import ch.ribeironelson.kargobike.database.entity.ClientEntity;

public class ClientWithAccounts {
    public ClientEntity client;

    public List<AccountEntity> accounts;
}
