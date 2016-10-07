<?php

class PEnquiry_Enquiry_Adminhtml_Rss_RssController extends Mage_Core_Controller_Front_Action
{
		public function enquiryAction()
		{

			$this->getResponse()->setHeader('Content-type', 'text/xml; charset=UTF-8');
			$this->loadLayout(false);
			$this->renderLayout();
		}
			
    public function preDispatch()
    {
		
			if ($this->getRequest()->getActionName() == 'enquiry') {
				$this->_currentArea = 'adminhtml';
				Mage::helper('rss')->authAdmin('mg/enquiry');
			}
			
        return parent::preDispatch();
    }
}
	
	