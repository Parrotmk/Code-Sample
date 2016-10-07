<?php
/**
 * @category   PMinifyJsCss
 * @package    PMinifyJsCss_Minify
 * @author     PMinifyJsCss
 * @copyright  Copyright (c) 2015 PMinifyJsCss (http://www.PMinifyJsCss.com)
 * @license    http://opensource.org/licenses/osl-3.0.php  Open Software License (OSL 3.0)
 */
class PMinifyJsCss_Minify_Block_About
    extends Mage_Adminhtml_Block_Abstract
    implements Varien_Data_Form_Element_Renderer_Interface
{

    /**
     * Render fieldset html
     *
     * @param Varien_Data_Form_Element_Abstract $element
     * @return string
     */
    public function render(Varien_Data_Form_Element_Abstract $element)
    {
    	$version  = Mage::helper('PMinifyJsCss_minify')->getExtensionVersion();
        $logopath =	'http://www.PMinifyJsCss.com/media/PMinifyJsCss.gif';
        $html = <<<HTML
<div style="background:url('$logopath') no-repeat scroll 15px 15px #e7efef; border:1px solid #ccc; min-height:100px; margin:5px 0; padding:15px 15px 15px 140px;">
	<p>
		<strong>PMinifyJsCss Minify HTML CSS JS Extension v$version</strong><br />
		Minify HTML CSS JS including inline CSS/JS and speed up your site. Works with default Magento CSS/JS merger.
	</p>
    <p>
        Website: <a href="http://www.PMinifyJsCss.com" target="_blank">www.PMinifyJsCss.com</a><br />
		Like, share and follow us on 
		<a href="https://www.facebook.com/PMinifyJsCss" target="_blank">Facebook</a>, 
		<a href="https://plus.google.com/+PMinifyJsCssCom" target="_blank">Google+</a>, 
		<a href="http://www.pinterest.com/PMinifyJsCss" target="_blank">Pinterest</a>, and 
        <a href="http://twitter.com/PMinifyJsCss" target="_blank">Twitter</a>.<br />
        If you have any questions send email at <a href="mailto:service@PMinifyJsCss.com">service@PMinifyJsCss.com</a>.
    </p>
</div>
HTML;
        return $html;
    }
}