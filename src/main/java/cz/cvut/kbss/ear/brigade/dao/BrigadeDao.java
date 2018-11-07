package cz.cvut.kbss.ear.brigade.dao;


import cz.cvut.kbss.ear.brigade.model.Brigade;
import org.springframework.stereotype.Repository;

@Repository
public class BrigadeDao extends BaseDao<Brigade> {

    protected BrigadeDao(Class<Brigade> type) {
        super(type);
    }

}
