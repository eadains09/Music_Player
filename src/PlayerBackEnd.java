import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;







import java.util.ArrayList;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;


public class PlayerBackEnd {
	
	private SongLibrary sl = new SongLibrary();
	private ArrayList<Song> nowPlaying = new ArrayList<Song>();
	private PlayerThread currThread;
	
	public void playSong(int selectedSong)
	{
		if (currThread != null)
		{
			currThread.stopPlaying();
			currThread.stop();
		}
		nowPlaying.add(0, getSong(selectedSong));
		currThread = new PlayerThread(nowPlaying.get(0).songPath);
		currThread.start();
	}
	
	public void addToQueue(int selectedSong)
	{
		nowPlaying.add(getSong(selectedSong));
	}
	
	public void clearNowPlaying()
	{
		nowPlaying = new ArrayList<Song>();
		if (currThread !=null)
		{
			currThread.stopPlaying();
		}
	}
	
	public void stopSong()
	{
		currThread.stopPlaying();
		currThread.stop();
	}
	
	public void playNextSong(int nextSong)
	{
		currThread = new PlayerThread(nowPlaying.get(nextSong).songPath);
		currThread.start();
	}
	
	public void loadAllMP3(File f)
	{
		if (f.isFile())
		{
			String extension = ".mp3";
			String testFile = f.getPath().substring(f.getPath().length()-4, f.getPath().length());
			if (testFile.equals(extension))
			{
				loadSingleMP3(f);
			}
		}else{
			File[] files = f.listFiles();
			for(File currFile: files)
			{
				loadAllMP3(currFile);
			}
		}
	}
	
	public void loadSingleMP3(File f)
	{
		boolean contains = false;
		for (int i=0; i<sl.librarySize(); i++)
		{
			if (sl.getSong(i).songPath.equals(f.getPath()))
			{
				contains=true;
				break;
			}
		}
		if (contains == false)
		{
			AudioFile af = null;
			try {
				af = AudioFileIO.read(f);
			} catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
				e.printStackTrace();
			}
			Tag tag = af.getTag();
			String title = tag.getFirst(FieldKey.TITLE);
			String artist = tag.getFirst(FieldKey.ARTIST);
			String album = tag.getFirst(FieldKey.ALBUM);
			String genre = tag.getFirst(FieldKey.GENRE);
		
			Song currSong = new Song (title, artist, album, genre, f.getPath());

			sl.addSong(currSong);
		}
	}
	
	public int librarySize()
	{
		return sl.librarySize();
	}
	
	public Song getSong(int i)
	{
		return sl.getSong(i);
	}
	
	public void sortSongs()
	{
		sl.sortSongLibrary();
	}
	
	public ArrayList<Song> search(String value)
	{
		ArrayList<Song> matchingSongs = new ArrayList<Song>();
		for (int i=0; i<sl.librarySize(); i++)
		{
			if (sl.getSong(i).songTitle.contains(value))
			{
				matchingSongs.add(sl.getSong(i));
			}
			if (sl.getSong(i).artist.contains(value))
			{
				if (!matchingSongs.contains(sl.getSong(i)))
				{
					matchingSongs.add(sl.getSong(i));
				}
			}
		}
		return matchingSongs;
	}
	
	public ArrayList<Song> getNowPlaying()
	{
		ArrayList<Song> nowPlayingCopy = new ArrayList<Song>();
		
		for(int i=0; i<nowPlaying.size(); i++)
		{
			nowPlayingCopy.add(nowPlaying.get(i));
		}
		
		return nowPlayingCopy;
	}
	
	public void saveLibrary(File f)
	{
		sl.saveLibrary(f);
	}
	
	public void loadLibrary(File f)
	{
		sl.loadLibrary(f);
	}
	
	class PlayerThread extends Thread{
		Player songPlayer;
		int currPlaying;
		
		PlayerThread(String filename){
			FileInputStream file;
			
				try {
					file = new FileInputStream(filename);
					songPlayer = new Player(file);
					currPlaying = 0;
				} catch (FileNotFoundException | JavaLayerException e) {
					e.printStackTrace();
				}
			
		}
	
		public void run()
		{
			try{
				songPlayer.play();
			}catch(Exception e){
				e.getMessage();
			}
			
			if (currPlaying < nowPlaying.size())
			{
				playNextSong(currPlaying+1);
			}
		}
		
		public void stopPlaying()
		{
			try{
				songPlayer.close();
			}catch(Exception e){
				e.getMessage();
			}
		}
		
		public void playNext(int nextSong)
		{
			FileInputStream nextSongFile = null;
			try {
				nextSongFile = new FileInputStream (nowPlaying.get(nextSong).songPath);
				songPlayer = new Player(nextSongFile);
			} catch (FileNotFoundException | JavaLayerException e) {
				e.printStackTrace();
			}
		}
	}
}
