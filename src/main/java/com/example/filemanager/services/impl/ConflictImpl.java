package com.example.filemanager.services.impl;

import com.example.filemanager.pojo.MergeScenario;
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
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConflictImpl implements ConflictServices {
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

}
