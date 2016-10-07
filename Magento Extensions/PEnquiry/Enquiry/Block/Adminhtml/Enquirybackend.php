<?php  
class PEnquiry_Enquiry_Block_Adminhtml_Enquirybackend extends Mage_Adminhtml_Block_Template {
public function __construct()
	{

	$this->_controller = "adminhtml_enquiry";
	$this->_blockGroup = "enquiry";
	$this->_headerText = Mage::helper("enquiry")->__("Activate Manage");
	parent::__construct();
    $this->_removeButton('add');
	
	}
}