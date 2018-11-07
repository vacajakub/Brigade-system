package cz.cvut.kbss.ear.brigade.dao.implementations;


import cz.cvut.kbss.ear.brigade.dao.interfaces.IBrigadeDao;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import org.springframework.stereotype.Repository;

@Repository
public class BrigadeDao extends BaseDao<Brigade> implements IBrigadeDao {

    public BrigadeDao() {
        super(Brigade.class);
    }

}
