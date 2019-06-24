package com.caiger.module.sys.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caiger.common.service.CrudService;
import com.caiger.module.sys.dao.AcreditDao;
import com.caiger.module.sys.entity.Acredit;
import com.caiger.module.sys.entity.Dse;
import com.caiger.module.sys.entity.User;
import com.caiger.module.sys.utils.ToolUtil;

@Service
public class AcreditService extends CrudService<AcreditDao, Acredit>{
  
	@Autowired
	private AcreditDao acreditDao;
	
	
	/**查找已授权设备
	 * @param user
	 * @return
	 */
    public List<Acredit> getDevList(Acredit acredit){
		return acreditDao.getDevList(acredit);
    }
    
    
    /**查找尚未授权设备,id不为空
	 * @param user
	 * @return
	 */
    
    public List<Acredit> getUnauthorizedDevList(List<String> list){
    	return acreditDao.getUnauthorizedDevList(list);
    }
    
    /**查找尚未授权设备,id为空
	 * @param user
	 * @return
	 */
    public List<Acredit> getUnauthorizedDevListWhereIdisNull(Acredit acredit){
    	return acreditDao.getUnauthorizedDevListWhereIdisNull(acredit);
    }
    
//    @Transactional(readOnly = false)
//    public void deleteBatches(List<String> list) {
//    	acreditDao.deleteBatches(list);
//    }
    
    @Transactional(readOnly = false)
    public void saveNewDev(String[] split,Dse dse,DseService dseService,Acredit acredit,User user,AcreditService acreditService) {
    	for (int i = 0; i < split.length; i++) {
			dse.setId(split[i]);
			Dse dseInfo = dseService.get(dse);
			acredit.setId(ToolUtil.get32UUID());
			acredit.setUserid(user.getId());
			acredit.setDevid(dseInfo.getId());
			acredit.setDevName(dseInfo.getDevName());
			acredit.setDevNumber(dseInfo.getDevNumber());
			acredit.setDevLocation(dseInfo.getDevLocation());
			acredit.setEmaddress(dseInfo.getEmaddress());
			
			acreditDao.saveNewAuthorizedDev(acredit);
		}
    }
}
