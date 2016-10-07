<?php   
class PEnquiry_Enquiry_Block_Enquiry extends Mage_Core_Block_Template{   
	public function __construct()
    {
        parent::__construct();
		$datasets=Mage::getModel('enquiry/enquiry')->getCollection();
        $this->setDatasets($datasets);
    }
    protected function _prepareLayout()
    {
        parent::_prepareLayout();
        $pager = $this->getLayout()->createBlock('page/html_pager')->setCollection($this->getDatasets());
        $this->setChild('pager', $pager);
        $this->getDatasets()->load();
        return $this;
    }
	public function addToTopLink() {
		
			$topBlock = $this->getParentBlock();
			if($topBlock) 
			{
				$topBlock->addLink($this->__('Activate'),'enquiry',
									'enquirysystem',true,array(),10);
			}	
	}
	
    public function getPagerHtml()
    {
        return $this->getChildHtml('pager');
    }

   public function getEnquiryActionUrl()
    {
        return Mage::getUrl('enquiry/enquiry/enquiry');
    }

}