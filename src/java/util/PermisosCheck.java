/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

/**
 *
 * @author Admin
 */
public class PermisosCheck {

        private String columnDetail;
        private boolean active;

        public PermisosCheck(String columnDetail,boolean active){
            this.columnDetail=columnDetail;
            this.active=active;
        }
        
    /**
     * @return the columnDetail
     */
    public String getColumnDetail() {
        return columnDetail;
    }

    /**
     * @param columnDetail the columnDetail to set
     */
    public void setColumnDetail(String columnDetail) {
        this.columnDetail = columnDetail;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }
        
}
