package com.googlecode.madschuelerturnier.business.spieldurchfuehrung;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.googlecode.madschuelerturnier.model.SpielZeile;
import com.googlecode.madschuelerturnier.persistence.repository.SpielZeilenRepository;

@Component("durchfuehrungDataDatabase")
public class DurchfuehrungDataDatabase implements SpielDurchfuehrungData {
	

	@Autowired
	private SpielZeilenRepository repo;

	
	
	/*
	
    data.getList2ZumVorbereiten().addAll(spielzeilenRepo.findBZurVorbereitung());
    data.getList3Vorbereitet().addAll(spielzeilenRepo.findCVorbereitet());
    data.getList4Spielend().addAll(spielzeilenRepo.findDSpielend());
	
	*/
	
	
	public DurchfuehrungDataDatabase(){
			
	}

	/* (non-Javadoc)
	 * @see com.googlecode.madschuelerturnier.business.spieldurchfuehrung.SpielDurchfuehrungData#getList1Wartend()
	 */
	public List<SpielZeile> getList1Wartend(int size) {
        Pageable p = new PageRequest(0,size);
        List<SpielZeile>  zeilen = this.repo.findNextZeilen(p);
        Collections.reverse(zeilen);
       return zeilen;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.madschuelerturnier.business.spieldurchfuehrung.SpielDurchfuehrungData#setList1Wartend(java.util.List)
	 */
	public void setList1Wartend(List<SpielZeile> list1Wartend) {
		this.repo.save(list1Wartend);
	}

	/* (non-Javadoc)
	 * @see com.googlecode.madschuelerturnier.business.spieldurchfuehrung.SpielDurchfuehrungData#getList2ZumVorbereiten()
	 */
	public List<SpielZeile> getList2ZumVorbereiten() {
		return repo.findBZurVorbereitung();

	}

	/* (non-Javadoc)
	 * @see com.googlecode.madschuelerturnier.business.spieldurchfuehrung.SpielDurchfuehrungData#setList2ZumVorbereiten(java.util.List)
	 */
	public void setList2ZumVorbereiten(List<SpielZeile> list2ZumVorbereiten) {
		this.repo.save(list2ZumVorbereiten);
	}

	/* (non-Javadoc)
	 * @see com.googlecode.madschuelerturnier.business.spieldurchfuehrung.SpielDurchfuehrungData#getList3Vorbereitet()
	 */
	public List<SpielZeile> getList3Vorbereitet() {
		return repo.findCVorbereitet();
	}

	/* (non-Javadoc)
	 * @see com.googlecode.madschuelerturnier.business.spieldurchfuehrung.SpielDurchfuehrungData#setList3Vorbereitet(java.util.List)
	 */
	public void setList3Vorbereitet(List<SpielZeile> list3Vorbereitet) {
		this.repo.save(list3Vorbereitet);
	}

	/* (non-Javadoc)
	 * @see com.googlecode.madschuelerturnier.business.spieldurchfuehrung.SpielDurchfuehrungData#getList4Spielend()
	 */
	public List<SpielZeile> getList4Spielend() {
		return repo.findDSpielend();
	}

	/* (non-Javadoc)
	 * @see com.googlecode.madschuelerturnier.business.spieldurchfuehrung.SpielDurchfuehrungData#setList4Spielend(java.util.List)
	 */
	public void setList4Spielend(List<SpielZeile> list4Spielend) {
		this.repo.save(list4Spielend);
	}

	/* (non-Javadoc)
	 * @see com.googlecode.madschuelerturnier.business.spieldurchfuehrung.SpielDurchfuehrungData#getList5Beendet()
	 */
	public List<SpielZeile> getList5Beendet() {
        return repo.findEBeendet();
	}

	/* (non-Javadoc)
	 * @see com.googlecode.madschuelerturnier.business.spieldurchfuehrung.SpielDurchfuehrungData#setList5Beendet(java.util.List)
	 */
	public void setList5Beendet(List<SpielZeile> list5Beendet) {
		this.repo.save(list5Beendet);
	}

}