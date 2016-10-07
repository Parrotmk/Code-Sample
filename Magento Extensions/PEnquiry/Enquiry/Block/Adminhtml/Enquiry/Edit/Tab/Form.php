<?php
class PEnquiry_Enquiry_Block_Adminhtml_Enquiry_Edit_Tab_Form extends Mage_Adminhtml_Block_Widget_Form
{
		protected function _prepareLayout() {
        parent::_prepareLayout();
        $this->setTemplate('enquiry/information.phtml');
    }

}
