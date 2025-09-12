package com.bit2025.mysite.service;

import org.springframework.stereotype.Service;

import com.bit2025.mysite.repository.SiteRepository;
import com.bit2025.mysite.vo.SiteVo;

@Service
public class SiteService {
	
	// site table의 하나의 행으로만 관리
	private static final Long CONFIG_ID = 1L;

	private SiteRepository siteRepository;

	public SiteService(SiteRepository siteRepository) {
		this.siteRepository = siteRepository;
	}

	public SiteVo getSite() {
		return siteRepository.findById(CONFIG_ID);
	}

	public void updateSite(SiteVo vo) {
		siteRepository.update(vo);
	}
	
}
