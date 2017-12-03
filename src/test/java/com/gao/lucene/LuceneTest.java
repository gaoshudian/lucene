package com.gao.lucene;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by gao on 2017/12/3.
 */
public class LuceneTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void importIndex() throws IOException{

        //索引库位置
        String indexPath = "/Users/gao/work/developer/workspace/github-workspace/lucene/src/main/resources/index_repository";
        //原始文档位置
        String sourceFilePath="/Users/gao/work/developer/workspace/github-workspace/lucene/src/main/resources/searchsource";
        //获得索引库的位置
        Path path = Paths.get(indexPath);
        //打开索引库
        FSDirectory dir = FSDirectory.open(path);
        //创建分词器
        Analyzer al = new StandardAnalyzer();
        //创建索引的写入的配置对象
        IndexWriterConfig iwc = new IndexWriterConfig(al);
        //创建索引的Writer
        IndexWriter iw = new IndexWriter(dir,iwc);
        //采集原始文档
        File sourceFile = new File(sourceFilePath);
        //获得文件夹下的所有文件
        File [] files = sourceFile.listFiles();
        //遍历每一个文件
        for(File file : files){
            //获得文件的属性
            String fileName = file.getName();
            String content = FileUtils.readFileToString(file,"UTF-8");
            long size = FileUtils.sizeOf(file);
            String path1 = file.getPath();

            Field fName = new TextField("fileName", fileName, Field.Store.YES);
            Field fcontent = new TextField("content", content, Field.Store.NO);
            Field fsize = new LongField("size", size, Field.Store.YES);
            Field fpath = new TextField("path", path1, Field.Store.YES);

            //创建文档对象
            Document doc = new Document();
            //把域加入到文档中
            doc.add(fName);
            doc.add(fcontent);
            doc.add(fsize);
            doc.add(fpath);
            //把文档写入到索引库
            iw.addDocument(doc);
        }
        //提交
        iw.commit();
        iw.close();


    }
}
