<?php
class PEnquiry_Enquiry_Block_Adminhtml_Enquiry_Edit extends Mage_Adminhtml_Block_Widget_Form_Container
{
		public function __construct()
		{
				parent::__construct();
				$this->_objectId = "enquiry_id";
				$this->_blockGroup = "enquiry";
				$this->_controller = "adminhtml_enquiry";
				//$this->_updateButton("save", "label", Mage::helper("enquiry")->__("Reply To Message"));
				$this->_updateButton("delete", "label", Mage::helper("enquiry")->__("Delete Activate"));
				$this->_removeButton('reset');		
		}

		public function getHeaderText()
		{
				if( Mage::registry("enquiry_data") && Mage::registry("enquiry_data")->getId() ){

				    return Mage::helper("enquiry")->__("View Activate", $this->htmlEscape(Mage::registry("enquiry_data")->getId()));

				} 
				else{

				     return Mage::helper("enquiry")->__("Add");

				}
		}
}