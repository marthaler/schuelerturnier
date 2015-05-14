package ch.emad.business.schuetu.spieldurchfuehrung;

import java.util.ArrayList;
import java.util.List;

import ch.emad.model.schuetu.model.SpielZeile;
import org.springframework.stereotype.Component;

@Component("durchfuehrungDataMemory")
public class SpielDurchfuehrungDataMemory implements SpielDurchfuehrungData {
	
	private List<SpielZeile> list1Wartend = new ArrayList<SpielZeile>();
	private List<SpielZeile> list2ZumVorbereiten =new ArrayList<SpielZeile>();
	private List<SpielZeile> list3Vorbereitet=new ArrayList<SpielZeile>();
	private List<SpielZeile> list4Spielend=new ArrayList<SpielZeile>();
	private List<SpielZeile> list5Beendet=new ArrayList<SpielZeile>();

	public SpielDurchfuehrungDataMemory(){
			
	}

	/* (non-Javadoc)
	 * @see SpielDurchfuehrungData#getList1Wartend()
	 */
	public List<SpielZeile> getList1Wartend(int size) {
		return list1Wartend;
	}

	/* (non-Javadoc)
	 * @see SpielDurchfuehrungData#setList1Wartend(java.util.List)
	 */
	public void setList1Wartend(List<SpielZeile> list1Wartend) {
		this.list1Wartend = list1Wartend;
	}

	/* (non-Javadoc)
	 * @see SpielDurchfuehrungData#getList2ZumVorbereiten()
	 */
	public List<SpielZeile> getList2ZumVorbereiten() {
		return list2ZumVorbereiten;
	}

	/* (non-Javadoc)
	 * @see SpielDurchfuehrungData#setList2ZumVorbereiten(java.util.List)
	 */
	public void setList2ZumVorbereiten(List<SpielZeile> list2ZumVorbereiten) {
		this.list2ZumVorbereiten = list2ZumVorbereiten;
	}

	/* (non-Javadoc)
	 * @see SpielDurchfuehrungData#getList3Vorbereitet()
	 */
	public List<SpielZeile> getList3Vorbereitet() {
		return list3Vorbereitet;
	}

	/* (non-Javadoc)
	 * @see SpielDurchfuehrungData#setList3Vorbereitet(java.util.List)
	 */
	public void setList3Vorbereitet(List<SpielZeile> list3Vorbereitet) {
		this.list3Vorbereitet = list3Vorbereitet;
	}

	/* (non-Javadoc)
	 * @see SpielDurchfuehrungData#getList4Spielend()
	 */
	public List<SpielZeile> getList4Spielend() {
		return list4Spielend;
	}

	/* (non-Javadoc)
	 * @see SpielDurchfuehrungData#setList4Spielend(java.util.List)
	 */
	public void setList4Spielend(List<SpielZeile> list4Spielend) {
		this.list4Spielend = list4Spielend;
	}

	/* (non-Javadoc)
	 * @see SpielDurchfuehrungData#getList5Beendet()
	 */
	public List<SpielZeile> getList5Beendet() {
		return list5Beendet;
	}

	/* (non-Javadoc)
	 * @see SpielDurchfuehrungData#setList5Beendet(java.util.List)
	 */
	public void setList5Beendet(List<SpielZeile> list5Beendet) {
		this.list5Beendet = list5Beendet;
	}

}