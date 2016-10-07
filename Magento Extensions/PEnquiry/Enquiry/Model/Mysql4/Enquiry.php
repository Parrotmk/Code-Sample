<?php
class PEnquiry_Enquiry_Model_Mysql4_Enquiry extends Mage_Core_Model_Mysql4_Abstract
{
    protected function _construct()
    {
        $this->_init("enquiry/enquiry", "enquiry_id");
    }
}