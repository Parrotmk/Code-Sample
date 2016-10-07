<?php
class PEnquiry_Enquiry_Adminhtml_EnquiryController extends Mage_Adminhtml_Controller_Action
{
		protected function _initAction()
		{
				$this->loadLayout()->_setActiveMenu("enquiry/enquiry")->_addBreadcrumb(Mage::helper("adminhtml")->__("Quick Enquiry Manage"),Mage::helper("adminhtml")->__("Quick Enquiry Manage"));
				return $this;
		}
		public function indexAction() 
		{
			    $this->_title($this->__("Activate"));
			    $this->_title($this->__("Activate Manage"));

				$this->_initAction();
				$this->renderLayout();
		}
		public function editAction()
		{			    
			    $this->_title($this->__("Activate"));
				$this->_title($this->__("Activate"));
			    $this->_title($this->__("Edit Item"));
				
				$id = $this->getRequest()->getParam("id");
				$model = Mage::getModel("enquiry/enquiry")->load($id);
				if ($model->getId()) {
					Mage::register("enquiry_data", $model);
					$this->loadLayout();
					$this->_setActiveMenu("enquiry/enquiry");
					$this->_addBreadcrumb(Mage::helper("adminhtml")->__("Activate Manager"), Mage::helper("adminhtml")->__("Activate Manage"));
					$this->_addBreadcrumb(Mage::helper("adminhtml")->__("Activate Description"), Mage::helper("adminhtml")->__("Enquiry Description"));
					$this->getLayout()->getBlock("head")->setCanLoadExtJs(true);
					$this->_addContent($this->getLayout()->createBlock("enquiry/adminhtml_enquiry_edit"))->_addLeft($this->getLayout()->createBlock("enquiry/adminhtml_enquiry_edit_tabs"));
					$this->renderLayout();
				} 
				else {
					Mage::getSingleton("adminhtml/session")->addError(Mage::helper("enquiry")->__("Item does not exist."));
					$this->_redirect("*/*/");
				}
		}

		public function newAction()
		{

		$this->_title($this->__("Activate"));
		$this->_title($this->__("Activate"));
		$this->_title($this->__("New Item"));

        $id   = $this->getRequest()->getParam("id");
		$model  = Mage::getModel("enquiry/enquiry")->load($id);

		$data = Mage::getSingleton("adminhtml/session")->getFormData(true);
		if (!empty($data)) {
			$model->setData($data);
		}

		Mage::register("enquiry_data", $model);

		$this->loadLayout();
		$this->_setActiveMenu("enquiry/enquiry");

		$this->getLayout()->getBlock("head")->setCanLoadExtJs(true);

		$this->_addBreadcrumb(Mage::helper("adminhtml")->__("Activate Manager"), Mage::helper("adminhtml")->__("Activate Manage"));
		$this->_addBreadcrumb(Mage::helper("adminhtml")->__("Activate Description"), Mage::helper("adminhtml")->__("Activate Description"));


		$this->_addContent($this->getLayout()->createBlock("enquiry/adminhtml_enquiry_edit"))->_addLeft($this->getLayout()->createBlock("enquiry/adminhtml_enquiry_edit_tabs"));

		$this->renderLayout();

		}
		public function saveAction()
		{

			$post_data=$this->getRequest()->getPost();
			$username=Mage::getStoreConfig('enquiries/settings/sendername');
			$senderemail=Mage::getStoreConfig('enquiries/settings/senderemail');
               
				if ($this->getRequest()->getParam("reply_message") && $username && $senderemail) {

					try {
						$model = Mage::getModel("enquiry/enquiry")
						->addData($post_data)
						->setId($this->getRequest()->getParam("id"))
						->setStatusId(1)
						->save();

						Mage::getSingleton("adminhtml/session")->addSuccess(Mage::helper("adminhtml")->__("Enquiry was successfully saved"));
						Mage::getSingleton("adminhtml/session")->setEnquiryData(false);

						if ($this->getRequest()->getParam("back")) {
							$this->_redirect("*/*/edit", array("id" => $model->getId()));
							return;
						}
						$this->sendMail($this->getRequest()->getParam("id"));
						$this->_redirect("*/*/");
						return;
					} 
					catch (Exception $e) {
						Mage::getSingleton("adminhtml/session")->addError($e->getMessage());
						Mage::getSingleton("adminhtml/session")->setEnquiryData($this->getRequest()->getPost());
						$this->_redirect("*/*/edit", array("id" => $this->getRequest()->getParam("id")));
					return;
					}
					

				}else{
			     if($username && $senderemail){
				Mage::getSingleton("adminhtml/session")->addError(Mage::helper("adminhtml")->__("Enter Reply Message "));
				}else{
				Mage::getSingleton("adminhtml/session")->addError(Mage::helper("adminhtml")->__("Fill Sender Name,Sender Email in Config "));
		 		}
				$this->_redirect("*/*/edit", array("id" => $this->getRequest()->getParam("id")));
				}
				
		}
		//added the code for saving the data in Email Template 
		public function sendMail($id) {
           $model = Mage::getModel("enquiry/enquiry")->load($id);
        $email = $model->getEmail();
		$name = $model->getName();
		$comment =$model->getComment();
		$telephone = $model->getTelephone();
        $reply =   $model->getReplyMessage();      
        try {
            $customerEmailId = $email;
            $customerName = $name;
            $emailTemplate = Mage::getModel('core/email_template')
                    ->loadDefault('comment_template');
            $emailTemplateVariables = array();
            $emailTemplateVariables['comment'] = $comment;
            $emailTemplateVariables['reply'] = $reply;
			$emailTemplateVariables['name'] = $customerName;
			$emailTemplateVariables['telephone'] = $telephone;
			$username=Mage::getStoreConfig('enquiries/settings/sendername');
			$senderemail=Mage::getStoreConfig('enquiries/settings/senderemail');
            $emailTemplate->setSenderName($username);
            $emailTemplate->setSenderEmail($senderemail);
            $emailTemplate->setType('html');
            $emailTemplate->setTemplateSubject($comment);
            $emailTemplate->send($customerEmailId, $customerName, $emailTemplateVariables);
            return true;
        } catch (Exception $e) {
            $errorMessage = $e->getMessage();
            return $errorMessage;
        }
    }



		public function deleteAction()
		{
				if( $this->getRequest()->getParam("id") > 0 ) {
					try {
						$model = Mage::getModel("enquiry/enquiry");
						$model->setId($this->getRequest()->getParam("id"))->delete();
						Mage::getSingleton("adminhtml/session")->addSuccess(Mage::helper("adminhtml")->__("Item was successfully deleted"));
						$this->_redirect("*/*/");
					} 
					catch (Exception $e) {
						Mage::getSingleton("adminhtml/session")->addError($e->getMessage());
						$this->_redirect("*/*/edit", array("id" => $this->getRequest()->getParam("id")));
					}
				}
				$this->_redirect("*/*/");
		}

		
		public function massRemoveAction()
		{
			try {
				$ids = $this->getRequest()->getPost('enquiry_ids', array());
				foreach ($ids as $id) {
                      $model = Mage::getModel("enquiry/enquiry");
					  $model->setId($id)->delete();
				}
				Mage::getSingleton("adminhtml/session")->addSuccess(Mage::helper("adminhtml")->__("Item(s) was successfully removed"));
			}
			catch (Exception $e) {
				Mage::getSingleton("adminhtml/session")->addError($e->getMessage());
			}
			$this->_redirect('*/*/');
		}
			
		/**
		 * Export order grid to CSV format
		 */
		public function exportCsvAction()
		{
			$fileName   = 'enquiry.csv';
			$grid       = $this->getLayout()->createBlock('enquiry/adminhtml_enquiry_grid');
			$this->_prepareDownloadResponse($fileName, $grid->getCsvFile());
		} 
		/**
		 *  Export order grid to Excel XML format
		 */
		public function exportExcelAction()
		{
			$fileName   = 'enquiry.xml';
			$grid       = $this->getLayout()->createBlock('enquiry/adminhtml_enquiry_grid');
			$this->_prepareDownloadResponse($fileName, $grid->getExcelFile($fileName));
		}
}
