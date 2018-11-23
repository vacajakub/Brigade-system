package cz.cvut.kbss.ear.brigade.dao.implementations;

import cz.cvut.kbss.ear.brigade.model.Address;
import org.springframework.stereotype.Repository;

@Repository
public class AddressDao extends BaseDao<Address>{
    protected AddressDao() {
        super(Address.class);
    }

}
