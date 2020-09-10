import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

import static org.apache.lucene.index.DirectoryReader.open;

public class LuceneCrud {
    public String[] id={"1","2","3" };
    public String[] quetion={"what is your name","what is your age","what is your birth date"};

    public  Directory directory;
   public StandardAnalyzer analyzer = new StandardAnalyzer();
   public   IndexWriterConfig iwc=new IndexWriterConfig(analyzer);




    /*------add------*/

  public void Setup() throws IOException
    {

        directory = new RAMDirectory();
        IndexWriter writer=getIndexWriter();
        for(int i=0;i<id.length;++i)
        {
            Document doc = new Document();
            doc.add(new TextField("id",id[i],Field.Store.YES));
            doc.add(new TextField("quetion",quetion[i],Field.Store.YES));
            writer.addDocument(doc);
           // System.out.println(doc);
        }

        writer.close();
    }
    public  IndexWriter getIndexWriter() throws IOException {
        return new IndexWriter(directory,iwc);
    }
    /*---------delete-------*/

   public void Delete() throws IOException{
        IndexWriter writer = getIndexWriter();
        writer.deleteDocuments(new Term("id","2"));
        writer.commit();
        writer.close();
    }

    /*-------update----*/

      public void Update() throws IOException {
        IndexWriter writer =getIndexWriter();
        Document doc=new Document();
        doc.add(new TextField("id","1",Field.Store.YES));
        doc.add(new TextField("quetion", "own", Field.Store.YES));
        writer.updateDocument(new Term("id","1"),doc);
        writer.close();
    }

    public void Search() throws IOException {
        Term term=new Term("quetion","age");
        Query q=new TermQuery(term);

        IndexReader reader = open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(q, 100);
        ScoreDoc[] hits = docs.scoreDocs;

        for (int i=0;i<hits.length;++i)
        {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println(d.get("quetion"));
        }
        reader.close();
    }
    public static void main(String[] args) throws IOException {

        LuceneCrud lc=new LuceneCrud();

        lc.Setup();
        lc.getIndexWriter();
        lc.Delete();
        lc.Update();
        lc.Search();
    }
}
