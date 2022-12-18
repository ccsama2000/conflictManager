package com.example.filemanager.services.impl;

import com.example.filemanager.dao.fileInfoMapper;
import com.example.filemanager.pojo.MergeScenario;
import com.example.filemanager.pojo.MergeTuple;
import com.example.filemanager.pojo.fileInfoWithBLOBs;
import com.example.filemanager.services.ConflictServices;
import com.example.filemanager.utils.FileUtils;
import com.example.filemanager.utils.GitUtils;
import com.example.filemanager.utils.PathUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.merge.RecursiveMerger;
import org.eclipse.jgit.merge.ThreeWayMerger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConflictImpl implements ConflictServices {

    @Autowired
    fileInfoMapper fileInfoMapper;

    @Override
    public byte[] getFileByCommitAndPath(String path, RevCommit commit, Repository repository) throws IOException {
        TreeWalk treeWalk = TreeWalk.forPath(repository, path, commit.getTree());
        if (treeWalk == null)
            return null;
        ObjectLoader objectLoader = repository.open(treeWalk.getObjectId(0));
        return objectLoader.getBytes();
    }


    @Override
    public List<MergeScenario> getAllConflictInfo(String path) throws Exception {
        GitUtils gitUtils=new GitUtils();
        Repository repository=gitUtils.getRepository(path);
        List<RevCommit> mergeCommits=gitUtils.getMergeCommits(repository);
        List<MergeScenario> scenarios=new ArrayList<>();
        for(int i=0;i<mergeCommits.size();i++){
            RevCommit commit=mergeCommits.get(i);
            System.out.println("analysing "+i);
                try {
                    scenarios.addAll(getMergeInfoByCommit(commit,repository));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
        }
        return scenarios;
    }

    @Override
    public List<MergeScenario> getMergeInfoByCommit(RevCommit merged, Repository repository) throws Exception {
        RevCommit p1 = merged.getParents()[0];
        RevCommit p2 = merged.getParents()[1];
        ThreeWayMerger merger = MergeStrategy.RECURSIVE.newMerger(repository, true);
        List<MergeScenario> scenarios=new ArrayList<>();

        if(!merger.merge(p1, p2)){
            RecursiveMerger rMerger = (RecursiveMerger)merger;
            RevCommit base = (RevCommit) rMerger.getBaseCommitId();
            rMerger.getMergeResults().forEach((file, result) -> {
                if(file.endsWith(".java") && result.containsConflicts()) {
//                    logger.info("conflicts were found in {}", file);
                    MergeScenario scenario = new MergeScenario(file);
                    try {
//                        logger.info("collecting scenario in merged commit {}", merged.getName());
                        if(getFileByCommitAndPath(file, p1,repository)!=null){
                            scenario.ours = new String(getFileByCommitAndPath(file, p1,repository));
                        }
                        if(getFileByCommitAndPath(file, p2,repository)!=null){
                            scenario.theirs = new String(getFileByCommitAndPath(file, p2,repository));
                        }
                        if(getFileByCommitAndPath(file, base,repository)!=null){
                            scenario.base = new String(getFileByCommitAndPath(file, base,repository));
                        }
                        scenarios.add(scenario);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        return scenarios;
    }

    @Override
    public List<MergeScenario> getSpecifiedConflictInfo(String path, String branch1, String branch2) throws Exception {
        GitUtils gitUtils=new GitUtils();
        Repository repository=gitUtils.getRepository(path);
        Git git=new Git(repository);
        ObjectId b1=git.getRepository().resolve(branch1);
        ObjectId b2=git.getRepository().resolve(branch2);
        RevCommit commit1=gitUtils.getSpecificCommits(repository,b1);
        RevCommit commit2=gitUtils.getSpecificCommits(repository,b2);

        List<MergeScenario> scenarios1=getMergeInfo(commit1,commit2,repository,path);
        return scenarios1;
    }


    @Override
    public List<MergeScenario> getMergeInfo(RevCommit commit1, RevCommit commit2, Repository repository,String path) throws Exception {
        ThreeWayMerger merger = MergeStrategy.RECURSIVE.newMerger(repository, true);
        List<MergeScenario> scenarios=new ArrayList<>();
        FileUtils fileUtils=new FileUtils();
        PathUtils pathUtils=new PathUtils();
        if(!merger.merge(commit1, commit2)){
            RecursiveMerger rMerger = (RecursiveMerger)merger;
            RevCommit base = (RevCommit) rMerger.getBaseCommitId();
            rMerger.getMergeResults().forEach((file, result) -> {
                if(file.endsWith(".java") && result.containsConflicts()) {
                    MergeScenario scenario = new MergeScenario(file);
                    try {
                        if(getFileByCommitAndPath(file, commit1,repository)!=null){
                            scenario.ours = new String(getFileByCommitAndPath(file, commit1,repository));
                        }
                        if(getFileByCommitAndPath(file, commit2,repository)!=null){
                            scenario.theirs = new String(getFileByCommitAndPath(file, commit2,repository));
                        }
                        if(getFileByCommitAndPath(file, base,repository)!=null){
                            scenario.base = new String(getFileByCommitAndPath(file, base,repository));
                        }
                        scenario.absPath=pathUtils.getFileWithPathSegment(path,file);
                        File conflict=new File(scenario.absPath);
                        scenario.conflict=fileUtils.readFile(conflict);
                        scenarios.add(scenario);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        return scenarios;
    }

    @Override
    public int saveMergeInfo(String path,String branch1,String branch2) throws Exception {
        GitUtils gitUtils=new GitUtils();
        Repository repository=gitUtils.getRepository(path);
        Git git=new Git(repository);
        ObjectId b1=git.getRepository().resolve(branch1);
        ObjectId b2=git.getRepository().resolve(branch2);
        RevCommit commit1=gitUtils.getSpecificCommits(repository,b1);
        RevCommit commit2=gitUtils.getSpecificCommits(repository,b2);
        ThreeWayMerger merger = MergeStrategy.RECURSIVE.newMerger(repository, true);
        List<MergeScenario> scenarios=new ArrayList<>();
        PathUtils pathUtils=new PathUtils();
        if(!merger.merge(commit1, commit2)){
            RecursiveMerger rMerger = (RecursiveMerger)merger;
            RevCommit base = (RevCommit) rMerger.getBaseCommitId();
            rMerger.getMergeResults().forEach((file, result) -> {
                if(file.endsWith(".java") && result.containsConflicts()) {
                    fileInfoWithBLOBs fileInfo=new fileInfoWithBLOBs(file,1);
                    try {
                        if(getFileByCommitAndPath(file, commit1,repository)!=null){
                            fileInfo.setOurs(getFileByCommitAndPath(file, commit1,repository));
                        }
                        if(getFileByCommitAndPath(file, commit2,repository)!=null){
                            fileInfo.setTheirs(getFileByCommitAndPath(file, commit2,repository));
                        }
                        if(getFileByCommitAndPath(file, base,repository)!=null){
                            fileInfo.setBase(getFileByCommitAndPath(file, base,repository));
                        }
                        fileInfo.setPath(pathUtils.getFileWithPathSegment(path,file));
//                        File conflict=new File(scenario.absPath);
//                        scenario.conflict=fileUtils.readFile(conflict);
                        fileInfoMapper.insert(fileInfo);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        return 1;
    }

    @Override
    public MergeScenario getSpecifiedFile(String fileName) throws Exception {
        MergeScenario mergeScenario=new MergeScenario(fileName);
        FileUtils fileUtils=new FileUtils();
        PathUtils pathUtils=new PathUtils();
        fileInfoWithBLOBs fileInfo=fileInfoMapper.selectByPrimaryKey(fileName);
        if(fileInfo.getOurs()!=null) {
            mergeScenario.ours = new String(fileInfo.getOurs());
        }
        if(fileInfo.getTheirs()!=null) {
            mergeScenario.theirs = new String(fileInfo.getTheirs());
        }
        if(fileInfo.getBase()!=null) {
            mergeScenario.base = new String(fileInfo.getBase());
        }
        //需要修改
//        File conflict=new File(fileInfo.getPath());
        Path tmpNoPrefix = Files.createTempDirectory(null);
        String path =tmpNoPrefix.toString();


        fileUtils.write(pathUtils.getFileWithPathSegment(path,"ours.java"),fileInfo.getOurs());
        fileUtils.write(pathUtils.getFileWithPathSegment(path,"theirs.java"),fileInfo.getTheirs());
        fileUtils.write(pathUtils.getFileWithPathSegment(path,"base.java"),fileInfo.getBase());
        File ours=new File(path,"ours.java");
        File theirs=new File(path,"theirs.java");
        File base=new File(path,"base.java");
        ProcessBuilder pb2 = new ProcessBuilder(
                "git",
                "merge-file",
                "--diff3",
                ours.getPath(),
                base.getPath(),
                theirs.getPath()
        );
        try {
            pb2.start().waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mergeScenario.conflict=fileUtils.readFile(ours);
        //删除临时文件
//        Files.delete(tmpNoPrefix);


        return mergeScenario;
    }

    @Override
    public List<MergeTuple> extractTuple(List<String> conflict, List<String> resolve) throws Exception {
        List<String> copy = new ArrayList<>();
        List<MergeTuple> tupleList = new ArrayList<>();
        FileUtils fileUtils=new FileUtils();
        int lenO=0,lenB=0,lenT=0;
        try {
            for (int i = 0, cnt = 0; i < conflict.size(); ++i) {
                //如果不是冲突块，则将这一行加入copy？然后继续，cnt++
                if (!conflict.get(i).startsWith("<<<<<<")) {
                    copy.add(conflict.get(i));
                    cnt++;
                    continue;
                }
                //如果是冲突块，则生成一个冲突块，mark为起始的行数（原文件）
                MergeTuple tuple = new MergeTuple();
                tuple.mark = cnt;
                //j，k分别为标记？
                int j = i, k = i;
                try {
                    while (!conflict.get(k).startsWith("||||||")) {
                        k++;
                    }
                    //k表示ours中冲突结束的位置，j此时为冲突开始的位置
                    tuple.startO=cnt+lenO;
                    lenO+=k-j-1;
                    tuple.endO=cnt+lenO;
                    tuple.ours = fileUtils.getCodeSnippets(conflict, j, k);
                    //j此时为下一个冲突，也就是base中冲突开始的位置
                    j = k;
                    while (!conflict.get(k).startsWith("======")) {
                        k++;
                    }
                    tuple.startB=cnt+lenB;
                    lenB+=k-j-1;
                    tuple.endB=cnt+lenB;
                    tuple.base = fileUtils.getCodeSnippets(conflict, j, k);

                    j = k;
                    while (!conflict.get(k).startsWith(">>>>>>")) {
                        k++;
                    }
                    tuple.startT=cnt+lenT;
                    lenT+=k-j-1;
                    tuple.endT=cnt+lenT;
                    tuple.theirs = fileUtils.getCodeSnippets(conflict, j, k);
                } catch (IndexOutOfBoundsException e) {
                }
                //从k继续遍历
                i = k;
                tupleList.add(tuple);
            }
        } catch (IndexOutOfBoundsException e) {
        }
        int rec[]=fileUtils.alignLines(copy,resolve);
        for(MergeTuple tuple : tupleList){
            int mark = tuple.mark;

            if(mark > 0 && mark < copy.size() && rec[mark - 1] != -1 && rec[mark] != -1){
                tuple.resolve = resolve.subList(rec[mark - 1] + 1, rec[mark]);
            }
        }
        return tupleList;
    }

}
