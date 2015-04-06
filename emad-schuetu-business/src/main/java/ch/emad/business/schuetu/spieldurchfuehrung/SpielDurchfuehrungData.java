package ch.emad.business.schuetu.spieldurchfuehrung;

import java.util.List;

import ch.emad.model.schuetu.model.SpielZeile;

public interface SpielDurchfuehrungData {

	public abstract List<SpielZeile> getList1Wartend(int size);

	public abstract void setList1Wartend(List<SpielZeile> list1Wartend);

	public abstract List<SpielZeile> getList2ZumVorbereiten();

	public abstract void setList2ZumVorbereiten(List<SpielZeile> list2ZumVorbereiten);

	public abstract List<SpielZeile> getList3Vorbereitet();

	public abstract void setList3Vorbereitet(List<SpielZeile> list3Vorbereitet);

	public abstract List<SpielZeile> getList4Spielend();

	public abstract void setList4Spielend(List<SpielZeile> list4Spielend);

	public abstract List<SpielZeile> getList5Beendet();

	public abstract void setList5Beendet(List<SpielZeile> list5Beendet);


}