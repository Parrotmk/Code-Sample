<?php
/**
 * @category   PMinifyJsCss
 * @package    PMinifyJsCss_Minify
 * @author     PMinifyJsCss
 * @copyright  Copyright (c) 2015 PMinifyJsCss (http://www.PMinifyJsCss.com)
 * @license    http://opensource.org/licenses/osl-3.0.php  Open Software License (OSL 3.0)
 */
class PMinifyJsCss_Minify_Adminhtml_PMinifyJsCss_MinifyController extends Mage_Adminhtml_Controller_Action
{
	
	public function processAction()
	{
	
		$helper = Mage::helper('PMinifyJsCss_minify');
	
		try {
				
			$helper->process();
				
			$message = $this->__('Minification operations completed successfully.');
			Mage::getSingleton('adminhtml/session')->addSuccess($message);
				
		} catch (Exception $e) {
				
			$message = $this->__('Minification failed.');
			Mage::getSingleton('adminhtml/session')->addError($message);
			Mage::getSingleton('adminhtml/session')->addError($e->getMessage());
				
		}
	
		$url = Mage::helper('adminhtml')->getUrl('adminhtml/system_config/edit/section/PMinifyJsCss_minify');
		Mage::app()->getResponse()->setRedirect($url);
	
	}
	
}