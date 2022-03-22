package assemblyline.client.guidebook.chapters;

import java.util.ArrayList;
import java.util.List;

import assemblyline.DeferredRegisters;
import electrodynamics.api.item.ItemUtils;
import electrodynamics.client.guidebook.utils.ItemWrapperObject;
import electrodynamics.client.guidebook.utils.components.Chapter;
import electrodynamics.client.guidebook.utils.components.Page;

public class ChapterMachines extends Chapter {

	private static final ItemWrapperObject LOGO = new ItemWrapperObject(17, 60, 2.0F, ItemUtils.fromBlock(DeferredRegisters.blockFarmer));
	
	@Override
	protected List<Page> genPages() {
		List<Page> pages = new ArrayList<>();
		pages.add(new Page());
		return pages;
	}

	@Override
	public ItemWrapperObject getLogo() {
		return LOGO;
	}

	@Override
	public String getTitleKey() {
		return "guidebook.assemblyline.chapter.machines";
	}
	
}
