<?php
class PEnquiry_Enquiry_Adminhtml_EnquirybackendController extends Mage_Adminhtml_Controller_Action
{
	public function indexAction()
    {
       $this->loadLayout();
	   $this->_title($this->__("Manage Enquiry"));
	   $this->renderLayout();
    }
}