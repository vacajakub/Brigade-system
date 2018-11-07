package cz.cvut.kbss.ear.brigade.dao;


import cz.cvut.kbss.ear.brigade.model.Brigade;
import org.springframework.stereotype.Repository;

@Repository
public class BrigadeDao extends BaseDao<Brigade> {

    public BrigadeDao() {
        super(Brigade.class);
    }

}
