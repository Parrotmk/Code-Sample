<?php
class PEnquiry_Enquiry_Block_Adminhtml_Enquiry_Grid extends Mage_Adminhtml_Block_Widget_Grid
{
		public function __construct()
		{
				parent::__construct();
				$this->setId("enquiryGrid");
				$this->setDefaultSort("enquiry_id");
				$this->setDefaultDir("ASC");
				$this->setSaveParametersInSession(true);
		}

		protected function _prepareCollection()
		{       
				$collection = Mage::getModel("enquiry/enquiry")
				->getCollection();
				$this->setCollection($collection);
				return parent::_prepareCollection();
		}
		protected function _prepareColumns()
		{
				$this->addColumn("enquiry_id", array(
				"header" => Mage::helper("enquiry")->__("ID"),
				"align" =>"right",
				"width" => "50px",
			    "type" => "number",
				"index" => "enquiry_id",
				));
                
				$this->addColumn("name", array(
				"header" => Mage::helper("enquiry")->__("Name"),
				"index" => "name",
				));
				
				$this->addColumn("created_date", array(
				"header" => Mage::helper("enquiry")->__("Date Added"),
				"index" => "created_date",
				));
				
					/*	$this->addColumn('status_id', array(
						'header' => Mage::helper('enquiry')->__('Status'),
						'index' => 'status_id',
						'type' => 'options',
						'options'=>PEnquiry_Enquiry_Block_Adminhtml_Enquiry_Grid::getOptionArray2(),				
						));
					*/				
                $this->addColumn('action',
                array(
                    'header'    =>  Mage::helper('enquiry')->__('Action'),
                    'width'     => '100',
                    'type'      => 'action',
                    'getter'    => 'getId',
                    'actions'   => array(
                        array(
                            'caption'   => Mage::helper('enquiry')->__('View'),
                            'url'       => array('base'=> '*/*/edit'),
                            'field'     => 'id'
                        )
                    ),
                    'filter'    => false,
                    'sortable'  => false,
                    'index'     => 'stores',
                    'is_system' => true,
                ));
									
      //  $this->addRssList('enquiry/adminhtml_rss_rss/enquiry', Mage::helper('enquiry')->__('RSS'));
      //  $this->addExportType('*/*/exportCsv', Mage::helper('sales')->__('CSV')); 
      //  $this->addExportType('*/*/exportExcel', Mage::helper('sales')->__('Excel'));

				return parent::_prepareColumns();
		}

		public function getRowUrl($row)
		{
			   return $this->getUrl("*/*/edit", array("id" => $row->getId()));
		}
		
		protected function _prepareMassaction()
		{
			$this->setMassactionIdField('enquiry_id');
			$this->getMassactionBlock()->setFormFieldName('enquiry_ids');
			$this->getMassactionBlock()->setUseSelectAll(true);
			$this->getMassactionBlock()->addItem('remove_enquiry', array(
					 'label'=> Mage::helper('enquiry')->__('Remove'),
					 'url'  => $this->getUrl('*/adminhtml_enquiry/massRemove'),
					 'confirm' => Mage::helper('enquiry')->__('Are you sure?')
				));
			return $this;
		}
			
		static public function getOptionArray2()
		{
            $data_array=array(); 
			$data_array[0]='Not Replied';
			$data_array[1]='Replied';
            return($data_array);
		}
		static public function getValueArray2()
		{
            $data_array=array();
			foreach(PEnquiry_Enquiry_Block_Adminhtml_Enquiry_Grid::getOptionArray2() as $k=>$v){
               $data_array[]=array('value'=>$k,'label'=>$v);		
			}
            return($data_array);

		}
}